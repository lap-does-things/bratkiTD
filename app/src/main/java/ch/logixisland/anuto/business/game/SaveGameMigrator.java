package ch.logixisland.anuto.business.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.logixisland.anuto.util.container.KeyValueStore;
// никому не нужная сомнительная имплементация миграции сейвов с прошедших версий. была полезна ровно 1 раз. 
public class SaveGameMigrator {

    private static final String TAG = GameLoader.class.getSimpleName();
// при новой версии менять это
    public static final int SAVE_GAME_VERSION = 2;

    private interface Migrator {
        boolean migrate(KeyValueStore gameState);
    }

    private final List<Migrator> mMigratorList = new ArrayList<>();

    public SaveGameMigrator() {
        mMigratorList.add(this::migrateToVersion2);
    }

    public boolean migrate(KeyValueStore gameState) {
        int version = gameState.getInt("version");
// если чел даунгрейднул игру
        if (version > SAVE_GAME_VERSION) {
            Log.w(TAG, "Save game version higher than required version!");
            return false;
        }

        while (version < SAVE_GAME_VERSION) {
           boolean result = mMigratorList.get(version - 1).migrate(gameState);

           if (!result) {
               Log.w(TAG, "Migration failed.");
               return false;
           }

           version++;
        }

        gameState.putInt("version", SAVE_GAME_VERSION);
        return true;
    }
// единственное по факту что тут есть. уже не нужно
    private boolean migrateToVersion2(KeyValueStore gameState) {
        if (gameState.getInt("lives") < 0) {
            gameState.putInt("finalScore", gameState.getInt("creditsEarned"));
        } else {
            gameState.putInt("finalScore", 0);
        }

        return true;
    }

}
