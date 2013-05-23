package com.example.providerexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.providerexample.database.DatabaseHandler;
import com.example.providerexample.database.Item;


/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The Item this fragment is presenting. (Item is my addition, mItem was there
	 */
	private Item mItem;

	/**
     * The UI elements showing the details of the Person
     */
    private TextView textTitle;    
	private CheckBox checkboxComplete;
    private TextView textDesc;
	
	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Should use the content provider here ideally.
			mItem = DatabaseHandler.getInstance(getActivity())
					.getItem(getArguments().getLong(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

		if (mItem != null) {
			textTitle = ((TextView) rootView.findViewById(R.id.textItemTitle));
			textTitle.setText(mItem.title);

			checkboxComplete = ((CheckBox) rootView.findViewById(R.id.fragmentDetailComplete));
			checkboxComplete.setChecked(mItem.complete);


			textDesc = ((TextView) rootView.findViewById(R.id.textDescription));
			textDesc.setText(mItem.description);
		}

		return rootView;
	}

    //Saves changes onPause
    @Override
    public void onPause() {
        super.onPause();
        updateItemFromUI();
    }

    private void updateItemFromUI() {
        if (mItem != null) {
            mItem.complete = checkboxComplete.isChecked();
            mItem.title = textTitle.getText().toString();
            mItem.description = textDesc.getText().toString();

            DatabaseHandler.getInstance(getActivity()).putItem(mItem);
        }
    }

}
