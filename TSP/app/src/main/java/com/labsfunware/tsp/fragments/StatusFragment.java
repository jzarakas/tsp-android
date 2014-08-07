package com.labsfunware.tsp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.labsfunware.tsp.C;
import com.labsfunware.tsp.MyActivity;
import com.labsfunware.tsp.R;
import com.labsfunware.tsp.api.BoardStatus;
import com.labsfunware.tsp.api.TSPClient;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StatusFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private View mView;

    private Handler mHandler = new Handler();

    public StatusFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StatusFragment newInstance(int sectionNumber) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);


        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        mView = rootView;

        loadStatus();

        Button refresh = (Button) mView.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStatus();
            }
        });

        Button haveToGo = (Button) mView.findViewById(R.id.have_to_go);
        haveToGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDialogFragment notificationDialogFragment = new NotificationDialogFragment();
                notificationDialogFragment.show(getFragmentManager(), "notifDialog");
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MyActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void loadStatus() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://agent.electricimp.com")
                .build();

        TSPClient tspClient = restAdapter.create(TSPClient.class);
        tspClient.getStatus(new Callback<BoardStatus>() {
            @Override
            public void success(BoardStatus boardStatus, Response response) {
                TextView toilet = (TextView) mView.findViewById(R.id.toilet);
                LinearLayout toiletLayout = (LinearLayout) mView.findViewById(R.id.toilet_ll);
                if (boardStatus.getPin2() > C.TOILET_THRESHOLD) {
                    toilet.setText("VACANT");
                    toiletLayout.setBackgroundColor(Color.GREEN);
                } else {
                    toilet.setText("OCCUPIED");
                    toiletLayout.setBackgroundColor(Color.RED);
                }
                toilet.append(" (" + boardStatus.getStall() + ")");

                TextView urinal = (TextView) mView.findViewById(R.id.urinal);
                LinearLayout urinalLayout = (LinearLayout) mView.findViewById(R.id.urinal_ll);
                if (boardStatus.getPin1() > C.URINAL_THRESHOLD) {
                    urinal.setText("VACANT");
                    urinalLayout.setBackgroundColor(Color.GREEN);
                } else {
                    urinal.setText("OCCUPIED");
                    urinalLayout.setBackgroundColor(Color.RED);

                }
                urinal.append(" (" + boardStatus.getUrinal() + ")");
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mHandler.postDelayed(refreshStatus, C.DEFAULT_STATUS_REFRESH);

    }

    private Runnable refreshStatus = new Runnable() {
        @Override
        public void run() {
            loadStatus();
        }
    };
}