package com.learn.note.practice.ideakeygen;

import org.junit.Test;

import static org.junit.Assert.*;

public class KeygenTest {

    @Test
    public void testMakeKey() throws Exception {
        System.out.println(Keygen.MakeKey("FUCK", 3650, 123));
    }
}