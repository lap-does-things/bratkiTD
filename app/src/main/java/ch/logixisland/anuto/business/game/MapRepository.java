package ch.logixisland.anuto.business.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.logixisland.anuto.R;

public class MapRepository {

    private final List<MapInfo> mMapInfos;

    public MapRepository() {
        mMapInfos = new ArrayList<>();
        mMapInfos.add(new MapInfo("original", 1, R.raw.map_original));
    }

    public List<MapInfo> getMapInfos() {
        return Collections.unmodifiableList(mMapInfos);
    }

    public MapInfo getMapById(String mapId) {
        for (MapInfo mapInfo : mMapInfos) {
            if (mapInfo.getMapId().equals(mapId)) {
                return mapInfo;
            }
        }

        throw new RuntimeException("Map not found!");
    }

    public String getDefaultMapId() {
        return "original";
    }
}
