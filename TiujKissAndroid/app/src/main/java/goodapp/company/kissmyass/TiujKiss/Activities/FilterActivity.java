package goodapp.company.kissmyass.TiujKiss.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import goodapp.company.kissmyass.R;

/**
 * Created by mehme on 30.07.2016.
 */
public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


    }
    public void goBack(View view){
        this.onBackPressed();
    }

}
