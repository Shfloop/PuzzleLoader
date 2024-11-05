package com.github.puzzle.game.events;

import com.badlogic.gdx.utils.Array;
import finalforeach.cosmicreach.networking.GamePacket;

public class OnPacketBucketRecieveIntercept {

    private Array<GamePacket> bucket;

    public void setPacketBucket(Array<GamePacket> bucket) {
        this.bucket = bucket;
    }

    public Array<GamePacket> getPacketBucket() {
        return bucket;
    }

}