package com.team2052.frckrawler.activities;

import android.os.Bundle;

import com.team2052.frckrawler.FRCKrawler;
import com.team2052.frckrawler.database.RxDBManager;
import com.team2052.frckrawler.di.DaggerFragmentComponent;
import com.team2052.frckrawler.di.FragmentComponent;
import com.team2052.frckrawler.subscribers.SubscriberModule;

import javax.inject.Inject;

public abstract class DatabaseActivity extends BaseActivity implements HasComponent {
    public static final String PARENT_ID = "PARENT_ID";
    FragmentComponent mComponent;
    @Inject
    RxDBManager rxDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
    }

    @Override
    public FragmentComponent getComponent() {
        if (mComponent == null) {
            FRCKrawler app = (FRCKrawler) getApplication();
            mComponent = DaggerFragmentComponent
                    .builder()
                    .fRCKrawlerModule(app.getModule())
                    .subscriberModule(new SubscriberModule(this))
                    .applicationComponent(app.getComponent())
                    .build();
        }
        return mComponent;
    }

    public abstract void inject();
}
