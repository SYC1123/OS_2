package com.example.os_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static bitmap bitmap = new bitmap();
    PCB_2 ready_head = new PCB_2("head", 0);
    PCB_2 block_head = new PCB_2("head", 0);
    PCB_2 run_head = new PCB_2("head", 0);
    private List<String> list ;
    String choose;
    int pager_number;
    int e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button create = findViewById(R.id.create);
        Button arrive = findViewById(R.id.arrive);
        Button block = findViewById(R.id.block);
        Button wake = findViewById(R.id.wake);
        Button end = findViewById(R.id.end);
        Button show = findViewById(R.id.show);
        Button showpage = findViewById(R.id.showpage);
        Button change = findViewById(R.id.change);
        Button readpage = findViewById(R.id.readpage);
        Button findf = findViewById(R.id.findf);
        final EditText name = findViewById(R.id.name);
        final EditText size1 = findViewById(R.id.size);
        final EditText editText = findViewById(R.id.editText);
        final TextView f = findViewById(R.id.f);
        final TextView save1 = findViewById(R.id.save1);
        final TextView save2 = findViewById(R.id.save2);
        TextView oldsave1 = findViewById(R.id.old_save1);
        TextView oldsave2 = findViewById(R.id.old_save2);
        RadioGroup radioGroup=findViewById(R.id.radioGroup);
        RadioButton LRU=findViewById(R.id.LRU);
        RadioButton FIFO=findViewById(R.id.FIFO);
        final Spinner spinner=findViewById(R.id.spinner);
        final Spinner spinner1=findViewById(R.id.spinner2);
        final TextView textView = findViewById(R.id.textView2);

        bitmap.init_bitmap();
        bitmap.show_bitmap(oldsave1, oldsave2);
        oldsave1.append("原来空间");
        oldsave2.append("原来空间");
        ready_head.setNext(null);
        block_head.setNext(null);
        run_head.setNext(null);

        //创建进程
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (run_head.hasNext()) {
                    int size = Integer.parseInt(size1.getText().toString());
                    PCB_2 pcb = new PCB_2(name.getText().toString(), size);
                    //Create_PCB(pcb);
                    int p = bitmap.add_bitmap(pcb, textView);
                    if (p == 1) {
                        bitmap.show_bitmap(save1, save2);
                        ready_head.addToTail(pcb);
                    }
                } else {
                    int size = Integer.parseInt(size1.getText().toString());
                    PCB_2 pcb = new PCB_2(name.getText().toString(), size);
                    // Create_PCB(pcb);
                    int p = bitmap.add_bitmap(pcb, textView);
                    if (p == 1) {
                        bitmap.show_bitmap(save1, save2);
                        run_head.setNext(pcb);
                    }
                    run_head.next.LRU.clear();
                    run_head.next.LRU.add(run_head.next.FIFO[2]);
                    run_head.next.LRU.add(run_head.next.FIFO[1]);
                    run_head.next.LRU.add(run_head.next.FIFO[0]);
                }
            }
        });
        //时间片到
        arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ready_head.addToTail(run_head.getNext());
                run_head.setNext(ready_head.deQueue(textView));
                show(ready_head, block_head, run_head, textView);

            }
        });
        //阻塞进程
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                block_head.addToTail(run_head.getNext());
                run_head.setNext(ready_head.deQueue(textView));
                show(ready_head, block_head, run_head, textView);
            }
        });
        //唤醒进程
        wake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PCB_2 tPcb = block_head.deQueue(textView);
                if (tPcb == null) {
                    textView.setText("当前无阻塞进程！");
                    //System.out.println("当前无阻塞进程！");
                } else {
                    if (run_head.getNext() == null) {
                        run_head.setNext(tPcb);
                    } else {
                        ready_head.addToTail(tPcb);
                    }
                }

                show(ready_head, block_head, run_head, textView);
            }
        });
        //终止进程
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (run_head.getNext() == null) {
                    textView.setText("当前无可终止进程！");
                    // System.out.println("当前无可终止进程！");
                } else {
                    bitmap.end_bitmap(run_head.getNext());
                    run_head.setNext(ready_head.deQueue(textView));
                }
                show(ready_head, block_head, run_head, textView);

            }
        });
        //显示进程
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(ready_head, block_head, run_head, textView);
            }
        });
        //显示页表
        showpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (run_head.next == null) {
                    //System.out.println("无执行进程！");
                    textView.setText("无执行进程！");
                } else {
                    textView.setText("");
                    for (int i = 0; i < run_head.next.getPage_table().length; i++) {
                        //System.out.println(run_head.next.page_table[i].toString());
                        textView.append(run_head.next.page_table[i].toString() + "\n");
                    }
                }
            }
        });
        //地址变换
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int t = Integer.parseInt(editText.getText().toString());
                if (run_head.next.page_table[t / 1024].physics_number == -1) {
                    // 置换算法
                    // run_head.next.page_table[t/1024].fail++;
                    //System.out.println("您访问的地址块不在内存，已使用FIFO算法进行置换");
                    textView.setText("您访问的地址块不在内存，已使用FIFO算法进行置换" + "\n");
                    run_head.next.exchange(bitmap, t / 1024);
                    //System.out.println("物理地址为：" + (run_head.next.page_table[t / 1024].physics_number * 1024 + t % 1024));
                    textView.append("物理地址为：" + (run_head.next.page_table[t / 1024].physics_number * 1024 + t % 1024));
                } else {
                    // run_head.next.page_table[t/1024].success++;
                    // System.out.println("物理地址为：" + (run_head.next.page_table[t / 1024].physics_number * 1024 + t % 1024));
                    textView.setText("物理地址为：" + (run_head.next.page_table[t / 1024].physics_number * 1024 + t % 1024));
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                list=new ArrayList<String>();
                for (int i = 0; i < run_head.next.page_table.length; i++) {
                    //System.out.print("页号" + i + "\t");
                    list.add("页号" + i );
                }
                ArrayAdapter<String> arrayAdapter=
                        new ArrayAdapter<String>(MainActivity.this,R.layout.item,R.id.text,list);
                spinner.setAdapter(arrayAdapter);
                spinner1.setAdapter(arrayAdapter);
                RadioButton radio_button =  (RadioButton)findViewById(checkedId);
                //Toast.makeText(MainActivity.this,radio_button.getText().toString(),Toast.LENGTH_SHORT).show();
                choose=radio_button.getText().toString();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pager_number=position;
                Log.d("123",position+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                e=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //置换算法
        readpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pager_number=spinner.getSelectedItemPosition();
                if(choose.equals("LRU")){
                    run_head.next.exchange_LRU(bitmap, pager_number);
                    textView.setText("");
                    for(int i=0;i<run_head.next.LRU.size();i++) {
                        //System.out.println(run_head.next.LRU.get(i));
                        textView.append(run_head.next.LRU.get(i)+"\n");
                    }
                }else{
                    run_head.next.exchange_FIFO(bitmap, pager_number);
                    textView.setText("");
                    for(int i=0;i<3;i++) {
                        textView.append(run_head.next.FIFO[i]+"\n");
                        //System.out.println(run_head.next.FIFO[i]);
                    }
                }
            }
        });
        //缺页率查询
        findf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_head.next.page_table[e].f(f);
            }
        });

    }

    public static void show(PCB ready_head, PCB block_head, PCB run_head, TextView textView) {
        textView.setText("");
        //bitmap.show_bitmap(textView);
        textView.append("执行队列：\n");
        if (run_head.getNext() == null) {
            textView.append("无执行进程！\n");
        } else {
            textView.append("PCB [name=" + run_head.getNext().getName() + ",大小="
                    + run_head.getNext().getSize() + "]\n");
        }
        textView.append("就绪队列：\n");
        if (ready_head.getNext() == null) {
            textView.append("就绪队列为空！\n");
        } else {
            ready_head.show(textView);
        }
        textView.append("阻塞队列：\n");
        if (block_head.getNext() == null) {
            textView.append("阻塞队列为空！\n");
        } else {
            block_head.show(textView);
        }

    }
}
