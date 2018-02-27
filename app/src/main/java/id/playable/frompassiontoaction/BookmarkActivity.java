package id.playable.frompassiontoaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import id.playable.frompassiontoaction.Model.BookmarkModel;
import id.playable.frompassiontoaction.components.DatabaseHandler;
import id.playable.frompassiontoaction.Adapter.BookmarkAdapter;

/**
 * Created by Zuhri Utama on 7/6/2015.
 */
public class BookmarkActivity extends AppCompatActivity {

    private DatabaseHandler db;
    private ListView list;

    private ArrayList<BookmarkModel> bookmarks = new ArrayList<>();
    private BookmarkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bookmark);

        list = findViewById(R.id.list);

        db = new DatabaseHandler(this);

        bookmarks = db.getBookmarks();

        adapter = new BookmarkAdapter(this, bookmarks, db);
        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = list.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                bookmarks.get(position).setChecked(checked);
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        db.deleteSelectedBookmark(adapter.getSelectedIds());
                        refresh();
                        mode.finish();
                        break;
                    case R.id.action_selectall:
                        for(BookmarkModel bookmark : bookmarks)
                            bookmark.setChecked(true);
                        mode.setTitle(bookmarks.size() + " Selected");
                        adapter.notifyDataSetChanged();
                        break;

                }
                return true;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.bookmark_action, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.clearSelections();
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookmarkModel bookmark = bookmarks.get(position);
                playBookmark(bookmark);
            }
        });

    }

    private void refresh() {
        bookmarks.clear();
        bookmarks.addAll(db.getBookmarks());
        adapter.notifyDataSetChanged();
    }

    private void playBookmark(BookmarkModel bookmark) {
        Intent i = new Intent();
        i.putExtra("bookmark", bookmark);

        setResult( RESULT_OK, i);
        finish();
    }
}
