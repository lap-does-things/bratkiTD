package ch.logixisland.anuto.business.tower;

import java.util.Iterator;

import ch.logixisland.anuto.GameSettings;
import ch.logixisland.anuto.engine.logic.GameEngine;
import ch.logixisland.anuto.entity.EntityTypes;
import ch.logixisland.anuto.entity.tower.Tower;
// ДЛЯ ТУПЫХ!!!! 
// Tower Aging - легендарная механика Братков ТД, которая не позволяет игрокам абузитт и продавать тавер за 100% потраченных на него денег. Значение меняется по ходу игры. 
public class TowerAging {

    private final GameEngine mGameEngine;

    public TowerAging(GameEngine gameEngine) {
        mGameEngine = gameEngine;
    }
// собственно оно самое
    public void ageTowers() {
        if (mGameEngine.isThreadChangeNeeded()) {
            mGameEngine.post(this::ageTowers);
            return;
        }

        Iterator<Tower> towers = mGameEngine
                .getEntitiesByType(EntityTypes.TOWER)
                .cast(Tower.class);

        while (towers.hasNext()) {
            Tower tower = towers.next();
            ageTower(tower);
        }
    }
// и изменение самой value 
    public void ageTower(final Tower tower) {
        if (mGameEngine.isThreadChangeNeeded()) {
            mGameEngine.post(() -> ageTower(tower));
            return;
        }

        int value = tower.getValue();
        value = Math.round(value * GameSettings.TOWER_AGE_MODIFIER);
        tower.setValue(value);
    }
}
