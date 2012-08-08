package org.wargamer2010.signshop.operations;

import org.bukkit.block.Block;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import java.util.logging.Level;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;
import org.wargamer2010.signshop.SignShop;
import org.wargamer2010.signshop.util.itemUtil;

public class Chest implements SignShopOperation {    
    private Boolean incorrectPar(SignShopArguments ssArgs) {
        ssArgs.get_ssPlayer().sendMessage("Could not complete operation, contact your System Administrator and checks the logs.");
        SignShop.log("Invalid Chest{}, check your config.yml!", Level.WARNING);
        return false;
    }
    
    @Override
    public Boolean setupOperation(SignShopArguments ssArgs) {
        if(ssArgs.operationParameters.isEmpty())         
            return incorrectPar(ssArgs);        
        Integer iChestnumber;
        try {
            iChestnumber = Integer.parseInt(ssArgs.operationParameters.get(0));
        } catch(NumberFormatException ex) {
            return incorrectPar(ssArgs);
        }
        if(iChestnumber < 1)
            return incorrectPar(ssArgs);
        
        ssArgs.forceMessageKeys.put("!items", ("!chest" + iChestnumber));
        
        Block bHolder = null;
        int iCount = 0;        
        for(Block bTemp : ssArgs.get_containables_root()) {
            if(bTemp.getState() instanceof InventoryHolder) {
                iCount++;
                if(iCount == iChestnumber)
                    bHolder = bTemp;                                
            }
        }
        if(bHolder == null) {
            ssArgs.get_ssPlayer().sendMessage("You need at least " + (iChestnumber) + " chest(s) to setup this shop!");
            return false;
        }
        
        List<Block> containables = new LinkedList();
        containables.add(bHolder);
        ssArgs.set_containables(containables);
        
        return true;
    }
    
    @Override
    public Boolean checkRequirements(SignShopArguments ssArgs, Boolean activeCheck) {
        return this.runOperation(ssArgs);
    }
    
    @Override
    public Boolean runOperation(SignShopArguments ssArgs) {        
        Integer iChestnumber;
        try {
            iChestnumber = Integer.parseInt(ssArgs.operationParameters.get(0));
        } catch(NumberFormatException ex) {
            return incorrectPar(ssArgs);
        }
        if(iChestnumber < 1)
            return incorrectPar(ssArgs);
        if(!ssArgs.miscSettings.containsKey(("chest" + iChestnumber)))
            return incorrectPar(ssArgs);        
        ssArgs.forceMessageKeys.put("!items", ("!chest" + iChestnumber));
        
        String misc = ssArgs.miscSettings.get(("chest" + iChestnumber));        
        String[] sItemss;
        if(!misc.contains(SignShopArguments.seperator)) {
            sItemss = new String[1];
            sItemss[0] = misc;
        } else
            sItemss = misc.split(SignShopArguments.seperator);
        ItemStack[] isItemss = null;        
            
        isItemss = itemUtil.convertStringtoItemStacks(Arrays.asList(sItemss));        
        ssArgs.set_isItems(isItemss);
        return true;
    }
}
