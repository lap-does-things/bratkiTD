package ch.logixisland.anuto.business.tower;

import java.util.Arrays;
import java.util.List;

import ch.logixisland.anuto.business.game.ScoreBoard;
import ch.logixisland.anuto.engine.logic.GameEngine;
import ch.logixisland.anuto.engine.logic.entity.EntityRegistry;
import ch.logixisland.anuto.entity.plateau.Plateau;
import ch.logixisland.anuto.entity.tower.Aimer;
import ch.logixisland.anuto.entity.tower.Tower;
import ch.logixisland.anuto.entity.tower.TowerStrategy;

public class TowerControl {
// опять объявляем всё нужное и подключаем
    private final GameEngine mGameEngine;
    private final ScoreBoard mScoreBoard;
    private final TowerSelector mTowerSelector;
    private final EntityRegistry mEntityRegistry;

    public TowerControl(GameEngine gameEngine, ScoreBoard scoreBoard, TowerSelector towerSelector,
                        EntityRegistry entityRegistry) {
        mGameEngine = gameEngine;
        mScoreBoard = scoreBoard;
        mTowerSelector = towerSelector;
        mEntityRegistry = entityRegistry;
    }
// апгрейд тавера
    public void upgradeTower() {
        if (mGameEngine.isThreadChangeNeeded()) {
            mGameEngine.post(this::upgradeTower);
            return;
        }
// если его можно апать
        Tower selectedTower = mTowerSelector.getSelectedTower();
        if (selectedTower == null || !selectedTower.isUpgradeable()) {
            return;
        }
// ура опять использование бесмысленных функций
        int upgradeCost = selectedTower.getUpgradeCost();
        if (upgradeCost > mScoreBoard.getCredits()) {
            return;
        }
// ну и получение апгрейда
        Tower upgradedTower = (Tower) mEntityRegistry.createEntity(selectedTower.getUpgradeName());
        mTowerSelector.showTowerInfo(upgradedTower);
        mScoreBoard.takeCredits(upgradeCost);
        Plateau plateau = selectedTower.getPlateau();
        selectedTower.remove();
        upgradedTower.setPlateau(plateau);
        upgradedTower.setValue(selectedTower.getValue() + upgradeCost);
        upgradedTower.setBuilt();
        mGameEngine.add(upgradedTower);
// и заменение наводки для него
        Aimer upgradedTowerAimer = upgradedTower.getAimer();
        Aimer selectedTowerAimer = selectedTower.getAimer();
        if (upgradedTowerAimer != null && selectedTowerAimer != null) {
            upgradedTowerAimer.setLockTarget(selectedTowerAimer.doesLockTarget());
            upgradedTowerAimer.setStrategy(selectedTowerAimer.getStrategy());
        }
    }
// улучшение тавера
    public void enhanceTower() {
        if (mGameEngine.isThreadChangeNeeded()) {
            mGameEngine.post(this::enhanceTower);
            return;
        }

        Tower selectedTower = mTowerSelector.getSelectedTower();
        if (selectedTower != null && selectedTower.isEnhanceable()) {
            if (selectedTower.getEnhanceCost() <= mScoreBoard.getCredits()) {
// суть та же, но не нужно новый еймер и т. д. объявлять  mScoreBoard.takeCredits(selectedTower.getEnhanceCost());
                selectedTower.enhance();
                mTowerSelector.updateTowerInfo();
            }
        }
    }
// прокликивает поведения тавера
    public void cycleTowerStrategy() {
        if (mGameEngine.isThreadChangeNeeded()) {
            mGameEngine.post(this::cycleTowerStrategy);
            return;
        }

        Tower selectedTower = mTowerSelector.getSelectedTower();
        if (selectedTower == null) {
            return;
        }

        Aimer selectedTowerAimer = selectedTower.getAimer();
        if (selectedTowerAimer == null) {
            return;
        }

        List<TowerStrategy> values = Arrays.asList(TowerStrategy.values());
        int index = values.indexOf(selectedTowerAimer.getStrategy()) + 1;

        if (index >= values.size()) {
            index = 0;
        }

 // устанавливаем поведение      selectedTowerAimer.setStrategy(values.get(index));
        mTowerSelector.updateTowerInfo();
    }
// меняем захват врага
    public void toggleLockTarget() {
        if (mGameEngine.isThreadChangeNeeded()) {
            mGameEngine.post(this::toggleLockTarget);
            return;
        }

        Tower selectedTower = mTowerSelector.getSelectedTower();
        if (selectedTower == null) {
            return;
        }

        Aimer selectedTowerAimer = selectedTower.getAimer();
        if (selectedTowerAimer == null) {
            return;
        }

        boolean lock = selectedTowerAimer.doesLockTarget();
        selectedTowerAimer.setLockTarget(!lock);
        mTowerSelector.updateTowerInfo();
    }
// продаем тавер
    public void sellTower() {
        if (mGameEngine.isThreadChangeNeeded()) {
            mGameEngine.post(this::sellTower);
            return;
        }

        Tower selectedTower = mTowerSelector.getSelectedTower();
        if (selectedTower != null) {
            mScoreBoard.giveCredits(selectedTower.getValue(), false);
            mGameEngine.remove(selectedTower);
        }
    }

}
