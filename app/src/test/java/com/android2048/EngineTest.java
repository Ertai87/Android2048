package com.android2048;

import android.util.Log;

import com.android2048.common.Constants2048;
import com.android2048.model.ArrayModel;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Sorter;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lyle.waldman on 07/03/2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class EngineTest {
    Engine testEngine;

    @Mock
    Log log;

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        testEngine = new Engine(ArrayModel.class, 4, 4);
    }

    @Test
    public void testPushUpOne(){
        testEngine.board.set(0, 3, 2);
        testEngine.pushUp();
        Assert.assertEquals(2, testEngine.board.get(0, 0));
    }

    @Test
    public void testPushUpMulti(){
        Random gen = new Random();
        int numcols = gen.nextInt() % 3 + 2;
        List<Integer> cols = new ArrayList<Integer>();
        for (int i=0; i < numcols; i++){
            int col = gen.nextInt() % 4;
            if (col < 0) col = -col;
            if (!cols.contains(col)){
                cols.add(col);
            }else{
                i--;
            }
        }
        for (int i=0; i < numcols; i++){
            testEngine.board.set(cols.get(i), 3, 2);
        }
        testEngine.pushUp();
        for (int i=0; i < numcols; i++){
            Assert.assertEquals(2, testEngine.board.get(cols.get(i), 0));
        }
    }

    @Test
    public void testPushUpCombine(){
        testEngine.board.set(0, 0, 2);
        testEngine.board.set(0, 1, 2);
        testEngine.pushUp();
        Assert.assertEquals(4, testEngine.board.get(0, 0));
    }

    @Test
    public void testPushUpCombineSkipSquares(){
        testEngine.board.set(0, 1, 2);
        testEngine.board.set(0, 3, 2);
        testEngine.pushUp();
        Assert.assertEquals(4, testEngine.board.get(0, 0));
    }

    @Test
    public void testPushUpCombineOnce(){
        testEngine.board.set(0, 1, 2);
        testEngine.board.set(0, 2, 2);
        testEngine.board.set(0, 3, 2);
        testEngine.pushUp();
        Assert.assertEquals(4, testEngine.board.get(0, 0));
        Assert.assertEquals(2, testEngine.board.get(0, 1));
    }

    @Test
    public void testPushDownOne(){
        testEngine.board.set(0, 0, 2);
        testEngine.pushDown();
        Assert.assertEquals(2, testEngine.board.get(0, 3));
    }

    @Test
    public void testPushDownMulti(){
        Random gen = new Random();
        int numcols = gen.nextInt() % 3 + 2;
        List<Integer> cols = new ArrayList<Integer>();
        for (int i=0; i < numcols; i++){
            int col = gen.nextInt() % 4;
            if (col < 0) col = -col;
            if (!cols.contains(col)) {
                cols.add(col);
            }else{
                i--;
            }
        }
        for (int i=0; i < numcols; i++){
            testEngine.board.set(cols.get(i), 0, 2);
        }
        testEngine.pushDown();
        for (int i=0; i < numcols; i++){
            Assert.assertEquals(2, testEngine.board.get(cols.get(i), 3));
        }
    }

    @Test
    public void testPushDownCombine(){
        testEngine.board.set(0, 2, 2);
        testEngine.board.set(0, 3, 2);
        testEngine.pushDown();
        Assert.assertEquals(4, testEngine.board.get(0, 3));
    }

    @Test
    public void testPushDownCombineSkipSquares(){
        testEngine.board.set(0, 0, 2);
        testEngine.board.set(0, 2, 2);
        testEngine.pushDown();
        Assert.assertEquals(4, testEngine.board.get(0, 3));
    }

    @Test
    public void testPushDownCombineOnce(){
        testEngine.board.set(0, 0, 2);
        testEngine.board.set(0, 1, 2);
        testEngine.board.set(0, 2, 2);
        testEngine.pushDown();
        Assert.assertEquals(4, testEngine.board.get(0, 3));
        Assert.assertEquals(2, testEngine.board.get(0, 2));
    }

    @Test
    public void testPushLeftOne(){
        testEngine.board.set(3, 0, 2);
        testEngine.pushLeft();
        Assert.assertEquals(2, testEngine.board.get(0, 0));
    }

    @Test
    public void testPushLeftMulti(){
        Random gen = new Random();
        int numrows = gen.nextInt() % 3 + 2;
        List<Integer> rows = new ArrayList<Integer>();
        for (int i=0; i < numrows; i++){
            int row = gen.nextInt() % 4;
            if (row < 0) row = -row;
            if (!rows.contains(row)) {
                rows.add(row);
            }else{
                i--;
            }
        }
        for (int i=0; i < numrows; i++){
            testEngine.board.set(3, rows.get(i), 2);
        }
        testEngine.pushLeft();
        for (int i=0; i < numrows; i++){
            Assert.assertEquals(2, testEngine.board.get(0, rows.get(i)));
        }
    }

    @Test
    public void testPushLeftCombine(){
        testEngine.board.set(0, 0, 2);
        testEngine.board.set(1, 0, 2);
        testEngine.pushLeft();
        Assert.assertEquals(4, testEngine.board.get(0, 0));
    }

    @Test
    public void testPushLeftCombineSkipSquares(){
        testEngine.board.set(1, 0, 2);
        testEngine.board.set(3, 0, 2);
        testEngine.pushLeft();
        Assert.assertEquals(4, testEngine.board.get(0, 0));
    }

    @Test
    public void testPushLeftCombineOnce(){
        testEngine.board.set(1, 0, 2);
        testEngine.board.set(2, 0, 2);
        testEngine.board.set(3, 0, 2);
        testEngine.pushLeft();
        Assert.assertEquals(4, testEngine.board.get(0, 0));
        Assert.assertEquals(2, testEngine.board.get(1, 0));
    }

    @Test
    public void testPushRightOne(){
        testEngine.board.set(0, 0, 2);
        testEngine.pushRight();
        Assert.assertEquals(2, testEngine.board.get(3, 0));
    }

    @Test
    public void testPushRightMulti(){
        Random gen = new Random();
        int numrows = gen.nextInt() % 3 + 2;
        List<Integer> rows = new ArrayList<Integer>();
        for (int i=0; i < numrows; i++){
            int row = gen.nextInt() % 4;
            if (row < 0) row = -row;
            if (!rows.contains(row)) {
                rows.add(row);
            }else{
                i--;
            }
        }
        for (int i=0; i < numrows; i++){
            testEngine.board.set(0, rows.get(i), 2);
        }
        testEngine.pushRight();
        for (int i=0; i < numrows; i++){
            Assert.assertEquals(2, testEngine.board.get(3, rows.get(i)));
        }
    }

    @Test
    public void testPushRightCombine(){
        testEngine.board.set(3, 0, 2);
        testEngine.board.set(2, 0, 2);
        testEngine.pushRight();
        Assert.assertEquals(4, testEngine.board.get(3, 0));
    }

    @Test
    public void testPushRightCombineSkipSquares(){
        testEngine.board.set(0, 0, 2);
        testEngine.board.set(2, 0, 2);
        testEngine.pushRight();
        Assert.assertEquals(4, testEngine.board.get(3, 0));
    }

    @Test
    public void testPushRightCombineOnce(){
        testEngine.board.set(0, 0, 2);
        testEngine.board.set(1, 0, 2);
        testEngine.board.set(2, 0, 2);
        testEngine.pushRight();
        Assert.assertEquals(4, testEngine.board.get(3, 0));
        Assert.assertEquals(2, testEngine.board.get(2, 0));
    }
}
