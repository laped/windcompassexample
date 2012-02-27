package dk.laped.windcompass;

import android.app.ListActivity;
import android.os.Bundle;

public class MainActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TestListAdapter adapter = new TestListAdapter(this);        
        adapter.add(new TestObject("Name 1", 7, 45, true));
        adapter.add(new TestObject("Name 2", 195, 75, false));
        adapter.add(new TestObject("Name 3", 12, 150, true));
        adapter.add(new TestObject("Name 4", 96, 265, false));
        adapter.add(new TestObject("Name 5", 84, 190, true));
        adapter.add(new TestObject("Name 6", 16, 190, false));
        adapter.add(new TestObject("Name 7", 8, 190, true));
        adapter.add(new TestObject("Name 8", 24, 190, false));
        adapter.add(new TestObject("Name 9", 48, 190, true));
        adapter.add(new TestObject("Name 10", 63, 190, true));
        
        setListAdapter(adapter);
    }
}