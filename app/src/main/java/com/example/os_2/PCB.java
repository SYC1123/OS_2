package com.example.os_2;

import android.widget.TextView;

public class PCB {
    protected String name;
    protected int begin;
    protected int end;
    protected int size;
    protected PCB next;

    public PCB(String name, int size) {
        // TODO 自动生成的构造函数存根
        this.name = name;
        this.size = size;
    }

    public PCB() {
        // TODO 自动生成的构造函数存根
    }
    public PCB(PCB pcb) {
        // TODO 自动生成的构造函数存根
        this.name=pcb.name;
        this.size=pcb.size;
    }

    public String getName() {
        return name;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public PCB getNext() {
        return next;
    }

    public void setNext(PCB next) {
        this.next = next;
    }

    public boolean hasNext() {
        return this.next == null ? false : true;
    }

    public void addToTail(PCB pcb) {
        PCB t = this;
        while (t.hasNext()) {
            t = t.next;
        }
        t.next = pcb;

    }

    public void show(TextView textView) {
        PCB t = this;
        while (t.hasNext()) {
            t = t.next;
            textView.append(
                    "PCB [name=" + t.name + ", 起始地址=" + (t.begin+1) + ", 结束地址=" + t.end + ",大小=" + t.size + "]\n");
        }
    }

    // 出队
    public PCB deQueue(TextView textView) {
        PCB t = this.next;
        if (t == null) {
            textView.setText("队列无进程！");
            return null;
        } else {
            this.next = t.next;
            t.next = null;
            return t;
        }

    }

}
