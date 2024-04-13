package ch.logixisland.anuto.business.game;

public class MapInfo {

    private String mMapId;
    private int mMapNameResId;
    private int mMapDataResId;
// поддержка доп. карт, которые можно писать в джсон. у каждой айди может быть для возможного выбора
    MapInfo(String mapId, int mapNameResId, int mapDataResId) {
        mMapId = mapId;
        mMapNameResId = mapNameResId;
        mMapDataResId = mapDataResId;
    }

    public String getMapId() {
        return mMapId;
    }

    public int getMapNameResId() {
        return mMapNameResId;
    }

    public int getMapDataResId() {
        return mMapDataResId;
    }
}
