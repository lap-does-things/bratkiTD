package ch.bfh.anuto.game.objects.impl;

import android.graphics.Canvas;

import ch.bfh.anuto.R;
import ch.bfh.anuto.game.GameEngine;
import ch.bfh.anuto.game.Layers;
import ch.bfh.anuto.game.objects.Enemy;
import ch.bfh.anuto.game.objects.GameObject;
import ch.bfh.anuto.game.objects.Sprite;
import ch.bfh.anuto.game.objects.HomingShot;
import ch.bfh.anuto.util.math.Vector2;

public class CanonShot extends HomingShot {

    private final static float DAMAGE = 60f;
    private final static float MOVEMENT_SPEED = 4.0f;

    private final static float ROTATION_STEP = 360f / GameEngine.TARGET_FRAME_RATE;

    private float mAngle = 0f;

    private final Sprite mSprite;

    public CanonShot(Vector2 position, Enemy target) {
        setPosition(position);
        setTarget(target);

        mSprite = Sprite.fromResources(mGame.getResources(), R.drawable.canon_shot, 4);
        mSprite.setListener(this);
        mSprite.setIndex(mGame.getRandom().nextInt(4));
        mSprite.setMatrix(0.33f, 0.33f, null, null);
        mSprite.setLayer(Layers.SHOT);

        mSpeed = MOVEMENT_SPEED;
    }

    @Override
    public void init() {
        super.init();

        mGame.add(mSprite);
    }

    @Override
    public void clean() {
        super.clean();

        mGame.remove(mSprite);
    }

    @Override
    public void onDraw(Sprite sprite, Canvas canvas) {
        super.onDraw(sprite, canvas);

        canvas.rotate(mAngle);
    }

    @Override
    public void tick() {
        mDirection = getDirectionTo(mTarget);
        mAngle += ROTATION_STEP;

        super.tick();
    }

    @Override
    protected void onTargetLost() {
        this.remove();
    }

    @Override
    protected void onTargetReached() {
        mTarget.damage(DAMAGE);
        this.remove();
    }
}
