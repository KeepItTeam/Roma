package com.example.studentsimulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    //строковые
    private String name;
    private String city;
    private String university;
    private String gender;

    //числовые
    private int age;
    private int money;
    private int allIncome;
    private int energy;
    private int mood;
    private int stipendIncome;//стипендия
    private int jobIncome;//подработка
    private int otherIncome;//

    //бонусы
    private int luck; //шанс на сокращение количества вариантов на 1
    private int tipNumber;//кол-во шпаргалок
    private int stamina;//сокращение урона энергии в %
    private int cheerfullness;//сокращение урона настроению
    private int discount;//скидка в Пятёрочку

    //статистика обучения
    private int lecturesMissed;
    private int practicesMissed;
    private int daysToExam;
    ;

    //информация по проведению пар
    private boolean isLectureToBeVisited;
    private boolean isPracticeToBeVisited;


    //TODO данные по курсу

    //TODO нормальное создание студента

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        Log.e("НАЧАЛИ", "НАЧАЛИ");
        setContentView(R.layout.activity_main);

    }

    public void openTest(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

    public void showMoneyInformation(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Ваши финансы")
                .setMessage("Ваша сумма на счету: " + money + "\n" +
                        "Общий доход" + allIncome + "\n" +
                        "Доход от стипендии" + stipendIncome + "\n" +
                        "Доход от работы" + jobIncome + "\n" +
                        "Прочие доходы" + otherIncome + "\n"
                )
                .setPositiveButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void showEnergyInformation(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                energy + "/100",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showMoodInformation(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                mood + "/100",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void goToShop(View view) {
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }

    public void showCourseStatistics(View view) {
        //TODO статитстику бы
    }

    private void getData() {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        age = sPref.getInt("age", 15);
        money = sPref.getInt("money", 3000);
        allIncome = sPref.getInt("allIncome", 0);
        stipendIncome = sPref.getInt("stipendIncome", 3000);
        energy = sPref.getInt("energy", 85);
        mood = sPref.getInt("mood", 85);
        jobIncome = sPref.getInt("jobIncome", 0);
        otherIncome = sPref.getInt("otherIncome", 0);
        name = sPref.getString("name", "Макар");
        city = sPref.getString("city", "Кострома");
        university = sPref.getString("university", "ЯрГУ");
        gender = sPref.getString("gender", "m");
        luck = sPref.getInt("luck", 0);
        tipNumber = sPref.getInt("tipNumber", 0);
        stamina = sPref.getInt("stamina", 0);
        cheerfullness = sPref.getInt("cheerfullness", 0);
        discount = sPref.getInt("discount", 0);
        lecturesMissed = sPref.getInt("lecturesMissed", 0);
        practicesMissed = sPref.getInt("practicesMissed", 0);
        daysToExam = sPref.getInt("daysToExam", 1);
        Log.e("DATA",age+" "+money+" "+allIncome+" "+name+" "+gender+" "+luck+" "+daysToExam);
    }



    public void goStudying(View view) {
        //посетить ли лекцию и практику (чекбокс)
        final String[] lessonsArray = {"Лекция", "Практика"};
        final boolean[] checkedItemsArray = {false, false};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Выберите, куда идёте:")
                .setMultiChoiceItems(lessonsArray, checkedItemsArray,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                checkedItemsArray[which] = isChecked;
                            }
                        })
                .setPositiveButton("Готово",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // передача информации о пропущенных парах
                                if (!checkedItemsArray[0]) {
                                    lecturesMissed++;
                                }
                                if (!checkedItemsArray[1]) {
                                    practicesMissed++;
                                }
                                //сохранение этой информации
                                SharedPreferences sPref = getPreferences(MODE_PRIVATE);
                                SharedPreferences.Editor ed = sPref.edit();
                                ed.putInt("practicesMissed", practicesMissed);
                                ed.putInt("lecturesMissed", lecturesMissed);
                                ed.commit();
                                //пары для посещения
                                isLectureToBeVisited = checkedItemsArray[0];
                                isPracticeToBeVisited = checkedItemsArray[1];
                                Button nextDayButton = findViewById(R.id.nextDayButton);
                                nextDayButton.setEnabled(true);
                                if (isLectureToBeVisited) {
                                    nextDayButton.setText("К лекции");
                                } else if (isPracticeToBeVisited) {
                                    nextDayButton.setText("К практике");
                                } else {
                                    nextDayButton.setText("К следующему дню");
                                }

                            }
                        })

                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void goToNextAction(View view) {
        if (isLectureToBeVisited) {
            Log.e("НАЧАЛИ", "ЛЕКЦИЯ");
            //TODO запилить активити с лекцией
            if (mood > 3)
                mood -= 3;
            if (energy > 3)
                energy -= 3;
            SharedPreferences sPref = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putInt("mood", mood);
            ed.putInt("energy", energy);
            ed.commit();
            isLectureToBeVisited = false;
            Button nextDayButton = findViewById(R.id.nextDayButton);
            if (isPracticeToBeVisited)
                nextDayButton.setText("К практике");
            else if (daysToExam!=1)
                nextDayButton.setText("К следующему дню");
            else nextDayButton.setText("К экзамену");
        } else if (isPracticeToBeVisited) {
            //TODO определить тип практики и запустить её
            Log.e("НАЧАЛИ", "ПРАКТИКА");
            if (mood > 3)
                mood -= 3;
            if (energy > 3)
                energy -= 3;
            SharedPreferences sPref = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putInt("mood", mood);
            ed.putInt("energy", energy);
            //TODO не забыть обработать результат с практики
            ed.commit();
            isPracticeToBeVisited = false;
            Button nextDayButton = findViewById(R.id.nextDayButton);
            if (daysToExam!=1)
                nextDayButton.setText("К следующему дню");
            else nextDayButton.setText("К экзамену");
        }
        else {
            Log.e("НАЧАЛИ", "НИЧЕГО");
            Button nextDayButton = findViewById(R.id.nextDayButton);
            nextDayButton.setText("");
            nextDayButton.setEnabled(false);
            if (daysToExam == 1) {
                //TODO начать экзамен
            } else {//TODO произвести переход к следующему дню
            }
        }
    }
}