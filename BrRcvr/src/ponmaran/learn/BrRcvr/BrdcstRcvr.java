package ponmaran.learn.BrRcvr;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class BrdcstRcvr extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_brdcst_rcvr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_brdcst_rcvr, menu);
        return true;
    }
}

