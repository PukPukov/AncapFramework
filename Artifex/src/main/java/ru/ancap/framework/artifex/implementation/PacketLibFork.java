package ru.ancap.framework.artifex.implementation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

public class PacketLibFork {
    
    @RequiredArgsConstructor
    @Accessors(fluent = true) @Getter
    public enum CurrentUsage {
        
        TAB_COMPLETE_RECEIVE(PacketLib.PacketEvents),
        TAB_COMPLETE_SEND(PacketLib.PacketEvents),
        ;
        
        public final PacketLib packetLib;
        
        public enum PacketLib {
            ProtocolLib,
            PacketEvents
        }
        
    }
    
}