package com.example.os_2;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PCB_2 extends PCB {
    Page_table[] page_table;
    PCB_2 next;
    Page_table[] FIFO = new Page_table[3];
    List<Page_table> LRU = new ArrayList<>();

    public PCB_2() {
        // TODO 自动生成的构造函数存根
    }

    public PCB_2(PCB pcb) {
        super(pcb);
    }

    public PCB_2(String string, int i) {
        // TODO 自动生成的构造函数存根
        super(string, i);
    }

    public Page_table[] getPage_table() {
        return page_table;
    }

    public boolean hasNext() {
        return this.next == null ? false : true;
    }

    public void setPage_table(Page_table[] page_table) {
        this.page_table = page_table;
    }

    public void addToTail(PCB_2 pcb) {
        PCB_2 t = this;
        while (t.hasNext()) {
            t = t.next;
        }
        t.next = pcb;
    }

    public void showFIFO() {
        for (int i = 0; i < 3; i++) {
            System.out.print(FIFO[i].table_number + "\t");
        }
    }

    public void show() {
        PCB_2 t = this;
        while (t.hasNext()) {
            t = t.next;
            System.out.println("PCB [name=" + t.name + ", 所占大小=" + t.size + "]");
        }
    }

    public PCB_2 getNext() {
        return next;
    }

    public void setNext(PCB_2 next) {
        this.next = next;
    }
    public void show(TextView textView) {
        PCB_2 t = this;
        while (t.hasNext()) {
            t = t.next;
            textView.append(
                    "PCB [name=" + t.name + ",大小=" + t.size + "]\n");
        }
    }
    // 出队
    public PCB_2 deQueue(TextView textView) {
        PCB_2 t = this.next;
        if (t == null) {
            //System.out.println("队列无进程！");
            textView.setText("队列无进程！");
            return null;
        } else {
            this.next = t.next;
            t.next = null;
            return t;
        }
    }

    public void exchange_LRU(bitmap map, int location) {
        if (this.page_table[location].physics_number == -1) {
            // 置换算法
            this.page_table[location].fail++;
            System.out.println("您访问的地址块不在内存，已使用LRU算法进行置换");
            OK: for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 8; j++) {
                    if (map.changeBitmap[i][j] == 0) {
                        // 从外存去除
                        int a = page_table[location].P / 8;
                        int b = page_table[location].P % 8;
                        map.changeBitmap[a][b] = 0;
                        // 把最久的位置给需要访问的
                        int p = LRU.get(0).physics_number / 8;
                        int q = LRU.get(0).physics_number % 8;
                        // map.bitmap[p][q]=0;
                        page_table[location].physics_number = p * 8 + q;
                        page_table[location].P = -1;
                        LRU.get(0).physics_number = -1;
                        LRU.get(0).P = 8 * i + j;
                        map.changeBitmap[i][j] = 1;

                        LRU.remove(0);
                        LRU.add(page_table[location]);
                        break OK;
                    }
                }
            }
        } else {
            this.page_table[location].success++;
            for (int i = 0; i < LRU.size(); i++) {
                if (LRU.get(i).table_number == location) {
                    Page_table page_table = LRU.get(i);
                    LRU.remove(i);
                    LRU.add(page_table);
                }
            }
        }
    }

    public void exchange_FIFO(bitmap map, int location) {
        if (this.page_table[location].physics_number == -1) {
            // 置换算法
            this.page_table[location].fail++;
            // System.out.println("fail"+this.page_table[location].fail);
            System.out.println("您访问的地址块不在内存，已使用FIFO算法进行置换");
            this.exchange(map, location);
        } else {
            this.page_table[location].success++;
            // System.out.println("success"+this.page_table[location].success);
        }

    }

    public void exchange(bitmap map, int location) {
        if (map.change_null_size == 0) {
            // ？？？？？？？？无可替换位置的时候该怎么办
            System.out.println("外存无可替换位置！");
        } else {
            OK: for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 8; j++) {
                    if (map.changeBitmap[i][j] == 0) {
                        int a = page_table[location].P / 8;
                        int b = page_table[location].P % 8;
                        map.changeBitmap[a][b] = 0;
                        int p = FIFO[0].physics_number / 8;
                        int q = FIFO[0].physics_number % 8;
                        // map.bitmap[p][q]=0;
                        page_table[location].physics_number = p * 8 + q;
                        page_table[location].P = -1;
                        FIFO[0].physics_number = -1;
                        FIFO[0].P = 8 * i + j;
                        map.changeBitmap[i][j] = 1;
                        FIFO[0] = FIFO[1];
                        FIFO[1] = FIFO[2];
                        FIFO[2] = page_table[location];
                        break OK;
                    }
                }
            }
        }
    }

}

