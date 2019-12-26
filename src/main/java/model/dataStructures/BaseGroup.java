package main.java.model.dataStructures;

import main.java.model.Board;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseGroup extends Base {

    private Set<Base> bases;

    public BaseGroup(Board board, Base base){
        super((Cycle) base.getCycle().clone(), board, base.getOwnerId());
        this.bases = new HashSet<>();
        bases.add(base);
    }

    public void addBase(Base base){
        this.getCycle().cutOrAddBase(base);
        DotNode dn = base.getDotNode();
        while (dn !=null){
            if(!this.contains(dn.d)){
                dn.d.markAsInsideBase();
            }
            dn = dn.next;
        }
    }

    public void addBaseAndJoinBaseGroups(DotNode firstCommonDn, Base base, List<BaseGroup> baseGroupsToAdd){
        addBase(base);
        for(BaseGroup baseGroup : baseGroupsToAdd){
            firstCommonDn = this.getDotNode();
            while (!baseGroup.contains(firstCommonDn.d)){
                firstCommonDn = firstCommonDn.next;
            }
            addBase(baseGroup);
            this.getBoard().getBaseGroupsOfPlayers()[base.getOwnerId()].remove(baseGroup);
        }
    }
}
