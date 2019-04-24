package com.example.os_2;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Page_table {
    public int table_number;// 页表号
    public int physics_number;// 内存地址号
    public int P;// 外存地址号
    public int success;// 成功次数
    public int fail;// 失败次数

    public Page_table() {
        // TODO 自动生成的构造函数存根
        table_number = 0;
        physics_number = 0;
        P = 0;
        success = 0;
        fail = 0;
    }

    public void f(TextView textView) {
        DecimalFormat df = new DecimalFormat("0.00%");
        double b = fail + success;
        double a = fail / b;
        //System.out.println(df.format(a));
        textView.setText(df.format(a));
    }

    @Override
    public String toString() {
        return "Page_table [页号=" + table_number + ", 内存号=" + physics_number + ", 外存号=" + P + "]";
    }

    public int getTable_number() {
        return table_number;
    }

    public void setTable_number(int table_number) {
        this.table_number = table_number;
    }

    public int getPhysics_number() {
        return physics_number;
    }

    public void setPhysics_number(int physics_number) {
        this.physics_number = physics_number;
    }

    public int getP() {
        return P;
    }

    public void setP(int p) {
        P = p;
    }

}
