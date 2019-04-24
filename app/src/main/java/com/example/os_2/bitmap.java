package com.example.os_2;

import android.widget.TextView;

public class bitmap {

    int BLOCK_SIZE;
    int MEN_SIZE;
    int[][] bitmap;
    int[][] changeBitmap;
    int null_size;// 内存空闲数
    int change_null_size;// 外存空闲数

    public bitmap() {
        // TODO 自动生成的构造函数存根
        BLOCK_SIZE = 1024;
        MEN_SIZE = 64;
        bitmap = new int[MEN_SIZE / 8][MEN_SIZE / 8];
        changeBitmap = new int[16][8];
        null_size = 0;
        change_null_size = 0;
    }

    public void show_bitmap(TextView save1,TextView save2) {
       // System.out.println("位示图");
        save1.setText("");
        save1.append("内存空间"+"\n");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //System.out.print(bitmap[i][j] + "\t");
                save1.append(bitmap[i][j] + " ");
            }
            //System.out.println("");
            save1.append("\n");
        }
        //System.out.println("置换空间");
        save2.setText("");
        save2.append("置换空间"+"\n");
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 8; j++) {
                //System.out.print(changeBitmap[i][j] + "\t");
                save2.append(changeBitmap[i][j] + " ");
            }
            //System.out.println("");
            save2.append("\n");
        }
    }

    public void init_bitmap() {
        for (int i = 0; i < MEN_SIZE / 8; i++) {
            for (int j = 0; j < 8; j++) {
                bitmap[i][j] = ((int) (10 * Math.random())) % 2;
                if (bitmap[i][j] == 0) {
                    null_size++;
                }
            }
        }
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 8; j++) {
                changeBitmap[i][j] = ((int) (10 * Math.random())) % 2;
                if (changeBitmap[i][j] == 0) {
                    change_null_size++;
                }
            }
        }
    }

    public void end_bitmap(PCB_2 pcb_2) {
        for (int a = 0; a < pcb_2.page_table.length; a++) {
            if (pcb_2.page_table[a].physics_number != -1) {
                int i = pcb_2.page_table[a].physics_number / 8;
                int j = pcb_2.page_table[a].physics_number % 8;
                bitmap[i][j] = 0;
                null_size++;
            }
            if (pcb_2.page_table[a].P != -1) {
                int i = pcb_2.page_table[a].P / 8;
                int j = pcb_2.page_table[a].P % 8;
                changeBitmap[i][j] = 0;
                change_null_size++;
            }
        }

    }

    public int add_bitmap(PCB_2 pcb_2, TextView textView) {
        int a;
        if (pcb_2.getSize() % BLOCK_SIZE != 0) {
            a = (pcb_2.getSize() / BLOCK_SIZE) + 1;
        } else {
            if(pcb_2.getSize() / BLOCK_SIZE==0){
                a=1;
            }else {
                a = (pcb_2.getSize() / BLOCK_SIZE);
            }
        }
        // System.out.println(null_size);
        pcb_2.page_table = new Page_table[a];
        for (int i = 0; i < pcb_2.page_table.length; i++) {
            pcb_2.page_table[i] = new Page_table();
            pcb_2.page_table[i].table_number = i;
        }
        int x = 0;
        if (null_size > a && a > 3) {
            for (int i = 0; i < MEN_SIZE / 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (bitmap[i][j] == 0) {
                        pcb_2.page_table[x].physics_number = i * 8 + j;
                        pcb_2.page_table[x].P = -1;
                        pcb_2.FIFO[x] = pcb_2.page_table[x];
                        // System.out.println(pcb_2.FIFO[x].physics_number);
                        x++;
                        bitmap[i][j] = 1;
                        if (x == 3) {
                            null_size -= a;
                            if (change_null_size > a - 3) {
                                for (int q = 0; q < 16; q++) {
                                    for (int p = 0; p < 8; p++) {
                                        if (changeBitmap[q][p] == 0) {
                                            pcb_2.page_table[x].P = q * 8 + p;
                                            pcb_2.page_table[x].physics_number = -1;
                                            changeBitmap[q][p] = 1;
                                            x++;
                                            if (x == a) {
                                                return 1;
                                            }
                                        }
                                    }
                                }
                            } else {
                                //System.out.println("外存不足");
                                textView.setText("外存不足");
                            }
                        }
                    }
                }
            }
            return 1;
        } else if (null_size > a && a <= 3) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (bitmap[i][j] == 0) {
                        pcb_2.page_table[x].physics_number = i * 8 + j;
                        pcb_2.page_table[x].P = -1;
                        pcb_2.FIFO[x] = pcb_2.page_table[x];
                        // System.out.println(pcb_2.FIFO[x].physics_number);
                        x++;
                        bitmap[i][j] = 1;
                        if (x == 3) {
                            null_size -= 3;
                            return 1;
                        }
                    }
                }
            }
            return 1;
        } else {
            //System.out.println("内存不足");
            textView.setText("内存不足");
            return -1;
        }
    }

}
