package ch.logixisland.anuto.business.tower;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import ch.logixisland.anuto.business.game.ScoreBoard;
import ch.logixisland.anuto.engine.logic.GameEngine;
import ch.logixisland.anuto.engine.logic.entity.Entity;
import ch.logixisland.anuto.entity.EntityTypes;
import ch.logixisland.anuto.entity.tower.Tower;
import ch.logixisland.anuto.util.math.Vector2;

public class TowerSelector implements ScoreBoard.Listener, Entity.Listener, Tower.Listener {

    public interface TowerInfoView {
        void showTowerInfo(TowerInfo towerInfo);

        void hideTowerInfo();
    }

    public interface TowerBuildView {
        void toggleTowerBuildView();

        void hideTowerBuildView();
    }

    public interface Listener {
        void towerInfoShown();
    }
// ОПЯЯЯЯТЬ всё объявляем. 
    private final GameEngine mGameEngine;
    private final ScoreBoard mScoreBoard;

    private TowerInfoView mTowerInfoView;
    private TowerBuildView mTowerBuildView;

    private boolean mControlsEnabled;
    private TowerInfo mTowerInfo;
    private Tower mSelectedTower;

    private Collection<Listener> mListeners = new CopyOnWriteArrayList<>();
// нужно для выбора тавера
    public TowerSelector(GameEngine gameEngine, ScoreBoard scoreBoard) {
        mGameEngine = gameEngine;
        mScoreBoard = scoreBoard;
        mScoreBoard.addListener(this);
    }
// чтобы была инфа и кнопки действий
    public void setTowerInfoView(TowerInfoView view) {
        mTowerInfoView = view;
    }

    public void setTowerBuildView(TowerBuildView view) {
        mTowerBuildView = view;
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }
// если выбрано, то выбрано
    public boolean isTowerSelected() {
        return mSelectedTower != null;
    }

    public TowerInfo getTowerInfo() {
        return mTowerInfo;
    }

    public void toggleTowerBuildView() {
        if (mGameEngine.isThreadChangeNeeded()) {
            mGameEngine.post(this::toggleTowerBuildView);
            return;
        }

        hideTowerInfoView();

        if (mTowerBuildView != null) {
            mTowerBuildView.toggleTowerBuildView();
        }
    }
// выбирает нужный тавер на позиции
    public void selectTowerAt(Vector2 position) {
        if (mGameEngine.isThreadChangeNeeded()) {
            final Vector2 finalPosition = position;
            mGameEngine.post(() -> selectTowerAt(finalPosition));
            return;
        }

        Tower closest = (Tower) mGameEngine
                .getEntitiesByType(EntityTypes.TOWER)
                .min(Entity.distanceTo(position));

        if (closest != null && closest.getDistanceTo(position) < 0.6f) {
            selectTower(closest);
        } else {
            selectTower(null);
        }
    }
// сам выбор и показание
    public void selectTower(Tower tower) {
        if (mGameEngine.isThreadChangeNeeded()) {
            final Tower finalTower = tower;
            mGameEngine.post(() -> selectTower(finalTower));
            return;
        }

        hideTowerInfoView();
        hideTowerBuildView();

        if (tower != null) {
            if (mSelectedTower == tower) {
                showTowerInfoView();
            } else {
                setSelectedTower(tower);
            }
        } else {
            setSelectedTower(null);
        }
    }
// чтобы это можно было делать
    public void setControlsEnabled(final boolean enabled) {
        if (mGameEngine.isThreadChangeNeeded()) {
            mGameEngine.post(() -> setControlsEnabled(enabled));
            return;
        }

        mControlsEnabled = enabled;

        if (mTowerInfo != null) {
            updateTowerInfo();
        }
    }
// показывает статы
    void showTowerInfo(Tower tower) {
        if (mGameEngine.isThreadChangeNeeded()) {
            mGameEngine.post(this::showTowerInfoView);
            return;
        }

        setSelectedTower(tower);
        showTowerInfoView();
    }

    void updateTowerInfo() {
        if (mGameEngine.isThreadChangeNeeded()) {
            mGameEngine.post(this::updateTowerInfo);
            return;
        }

        if (mTowerInfo != null) {
            showTowerInfoView();
        }
    }
// и обновляет статы дамага нанесеного и тд
    @Override
    public void entityRemoved(Entity entity) {
        selectTower(null);
    }

    @Override
    public void damageInflicted(float totalDamage) {
        updateTowerInfo();
    }

    @Override
    public void propertiesChanged() {
        updateTowerInfo();
    }

    @Override
    public void creditsChanged(int credits) {
        if (mTowerInfo != null) {
            updateTowerInfo();
        }
    }

    @Override
    public void bonusChanged(int waveBonus, int earlyBonus) {

    }// чтобы ничего нмешало выбирать
    @Override
    public void livesChanged(int lives) {

    }

    Tower getSelectedTower() {
        return mSelectedTower;
    }

    private void setSelectedTower(Tower tower) {
        if (mSelectedTower != null) {
            mSelectedTower.removeListener((Tower.Listener) this);
            mSelectedTower.removeListener((Entity.Listener) this);
            mSelectedTower.hideRange();
        }

        mSelectedTower = tower;

        if (mSelectedTower != null) {
            mSelectedTower.addListener((Tower.Listener) this);
            mSelectedTower.addListener((Entity.Listener) this);
            mSelectedTower.showRange();
        }
    }

    private void showTowerInfoView() {
        mTowerInfo = new TowerInfo(
                mSelectedTower,
                mScoreBoard.getCredits(),
                mControlsEnabled
        );

        if (mTowerInfoView != null) {
            mTowerInfoView.showTowerInfo(mTowerInfo);

            for (Listener listener : mListeners) {
                listener.towerInfoShown();
            }
        }
    }

    private void hideTowerInfoView() {
        mTowerInfo = null;

        if (mTowerInfoView != null) {
            mTowerInfoView.hideTowerInfo();
        }
    }

    private void hideTowerBuildView() {
        if (mTowerBuildView != null) {
            mTowerBuildView.hideTowerBuildView();
        }
    }

}
