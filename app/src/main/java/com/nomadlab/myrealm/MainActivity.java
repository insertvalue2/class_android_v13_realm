package com.nomadlab.myrealm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/*

https://docs.mongodb.com/realm-legacy/docs/java/latest/
https://abss.tistory.com/152

*  - SQL query 를 몰라도 작성이 가능하다.(ORM : Object Relational Mapping)
- DBMS 에 대한 종속성을 줄일 수 있다.
- 성능이 뛰어나다.
- 라이브러리 크기때문에 앱 용량이 2~3 MB 증가한다.
*
* */
public class MainActivity extends AppCompatActivity {

    Button btnSave;
    Button btnLoad;
    Button btnDelete;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 키 값 : 관계를 형성하지 않기 때문에 간단하다.
        // 관계를 형성하기 때문에 복잡하다.
        //
        Realm.init(this); // 초기화 한다.
        // 이런 방식으로 초기화 한다 config
        // 빌더패턴 == 메서드 체이닝
//        RealmConfiguration realmConfiguration = new RealmConfiguration
//                .Builder().name("project").build();
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .name("schooldb")
                .allowWritesOnUiThread(true) // UI Thread 에서도 realm 에 접근할 수 있도록 한다.
                .deleteRealmIfMigrationNeeded() // 데이터를 전부 지워 버리겠다.
                .build();
        // 마이그레이션이 필요한 경우 지워 버리겠다. (마이그레이션 : 동기화 시켜 주겠다)
        // 이름   학번    학교      전화번호

        // 홍길동1   1    부산고
        // 홍길동2   2    부산중

        // 필드가 새로 생겼을때
        // 기존에 있던 데이터베이스와 새로 생긴 데이터 베이스를 합쳐 줘야 한다.
        // 이렇게 합치는 것을(이동한다) 마이그레이션 한다.
        Realm.setDefaultConfiguration(realmConfiguration);
        // realm 객체를 얻는 방법
        realm = Realm.getDefaultInstance();

        // 테이블을 만드는 방법

        initData();
        addEventListener();
    }

    private void addEventListener() {
        btnSave.setOnClickListener(view -> {
            // A 테이블에서 테이터를 가져온다. --> 10 (9) (동기화 처리)
            // B 테이블에서 테이터를 가져온다.
            // C 테이블에서 테이터를 가져온다.
            // 조합을 한다.
            // D 테이블에 저장을 한다.
//                Task Task = new Task("New Task");
            realm.executeTransaction(transactionRealm -> {
                School school = new School("부산대학교");
                school.setLocation("부산시금정구");
                transactionRealm.insert(school);
            });


        });
        btnLoad.setOnClickListener(view -> {
            realm.executeTransaction(transactionRealm -> {
                // 테이블 명
                // 전부 가져 옴
                School school = transactionRealm.where(School.class).findFirst();
                if (school != null) {
                    Log.d("TAG", school.toString());
                } else {
                    Log.d("TAG", "null");
                }

            });
        });

        btnDelete.setOnClickListener(view -> {
            realm.executeTransaction(transactionRealm -> {
                // 전체 row 를 찾고 전체 삭제 처리
                transactionRealm.where(School.class).findAll().deleteAllFromRealm();
            });
        });
    }

    private void initData() {
        btnSave = findViewById(R.id.btnSave);
        btnLoad = findViewById(R.id.btnLoad);
        btnDelete = findViewById(R.id.btnDelete);
    }
}