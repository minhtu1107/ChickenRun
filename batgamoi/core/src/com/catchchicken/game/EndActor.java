package com.catchchicken.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

/**
 * Created by tu.tranhienminh on 6/8/2016.
 */
public class EndActor extends Actor {

    private TextureRegion Lvup;
    //private TextureRegion Lvdown;
    private TextureRegion Congrat;
    private TextureRegion fade;
//    private boolean isLvup;
    private float offset;
    private Skeleton anim;
    private AnimationState animState;
    SkeletonRenderer renderer;
    PolygonSpriteBatch pbatch;
    EndActor()
    {
        //Lvup = Assets.UI_atlas.findRegion("Level-up");
        //Lvdown = Assets.UI_atlas.findRegion("Level-down");
        fade = Assets.Fade_atlas.findRegion("fade");
        Congrat = Assets.Congrat_atlas.findRegion("Vongxoay");
        offset = BoardScreen.scr_h*5/6;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/Boy/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(atlas);
        json.setScale(0.6f*BoardScreen.scale_ratio);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("data/Boy/skeleton.json"));
        anim = new Skeleton(skeletonData);
        AnimationStateData stateData = new AnimationStateData(skeletonData);
        animState = new AnimationState(stateData);
        renderer = new SkeletonRenderer();
        pbatch = new PolygonSpriteBatch();
    }

    public void SetLvup(boolean b, String name)
    {
        if(name == null)
        {
            Lvup = null;
            return;
        }
        if(b)
            Lvup = Assets.UI_atlas.findRegion("Level-up");
        else
            Lvup = Assets.UI_atlas.findRegion("Level-down");
        animState.setAnimation(0,name,true);
        anim.setSkin(Assets.Boy_skeleton.getSkin().getName());
        anim.updateWorldTransform();
        Vector2 offset = new Vector2(), size = new Vector2();
        anim.getBounds(offset, size);
        anim.setPosition(BoardScreen.scr_w/2, BoardScreen.scr_h/2 - size.y/2);
    }

    public boolean Ready()
    {
        if(Lvup != null)
            return true;
        return false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(fade, 0, 0, BoardScreen.scr_w, BoardScreen.scr_h);
        batch.draw(Lvup, BoardScreen.scr_w/2 - Lvup.getRegionWidth()*BoardScreen.scale_ratio/2,
                offset/* - Lvup.getRegionHeight()*BoardScreen.scale_ratio*/,
                Lvup.getRegionWidth()*BoardScreen.scale_ratio,
                Lvup.getRegionHeight()*BoardScreen.scale_ratio
        );

        offset -= 0;

        batch.draw(Congrat, BoardScreen.scr_w/2 - Congrat.getRegionWidth()*BoardScreen.scale_ratio/2,
                BoardScreen.scr_h/2 - Congrat.getRegionHeight()*BoardScreen.scale_ratio/2,
                Congrat.getRegionWidth()*BoardScreen.scale_ratio,
                Congrat.getRegionHeight()*BoardScreen.scale_ratio
        );

        batch.end();

        animState.update(Gdx.graphics.getDeltaTime());
        animState.apply(anim);
        anim.updateWorldTransform();

        pbatch.begin();
        renderer.draw(pbatch, anim);
        pbatch.end();

        batch.begin();
    }
}
