package com.htlkaindorf.feba6481.client;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by feba6481 on 19.12.15.
 */
public class CombiFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String CANDS_ORIGINAL = "originalCandidates";
    public static int currentNumber = 0;
    private String[] cands;
    private int points[];

    private ListView listView;

    public CombiFragment() {
    }

    public static CombiFragment newInstance(int sectionNumber, String[] cands) {
        CombiFragment fragment = new CombiFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putStringArray(CANDS_ORIGINAL, cands);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //INIT
        cands = getArguments().getStringArray(CANDS_ORIGINAL);
        points = new int[cands.length];
        for (int i = 0; i < points.length; i++)
            points[i] = 0;

        //VIEW
        View rootView = inflater.inflate(R.layout.fragment_vote_abteilungssprecher, container, false);

        //LIST
        listView = (ListView) rootView.findViewById(R.id.listViewAbt);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, cands);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                final String tempi = (String) parent.getAdapter().getItem(position);

                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setFilters(new InputFilter[]{new InputFilterMinMax("1", cands.length + "")});
                new AlertDialog.Builder(getActivity())
                        .setTitle("Vote for " + tempi)
                        .setMessage("Please enter points: \nPoints not assigned yet: " + getNotAssigned())
                        .setView(input)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (!pointsAssigned(Integer.parseInt(input.getText().toString()))) {
                                    if (points[position] == 0) {
                                        points[position] = Integer.parseInt(input.getText().toString());
                                        cands[position] = cands[position] + " VOTED: " + points[position];
                                    } else
                                        cands[position] = cands[position].substring(0, cands[position].length() - 1) + input.getText().toString();
                                    adapter.notifyDataSetChanged();
                                } else {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Error")
                                            .setMessage("You cannot assign the same amount of points to the Candidates")
                                            .setPositiveButton("Reassign points", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    int prevPos = pointsAssignedTo(Integer.parseInt(input.getText().toString()));
                                                    cands[prevPos] = cands[prevPos].substring(0, cands[prevPos].length() - "VOTED: 0".length());
                                                    points[prevPos] = 0;
                                                    if (points[position] == 0) {
                                                        points[position] = Integer.parseInt(input.getText().toString());
                                                        cands[position] = cands[position] + " VOTED: " + points[position];
                                                    } else {
                                                        cands[position] = cands[position].substring(0, cands[position].length() - 1) + input.getText().toString();
                                                        points[position] = Integer.parseInt(input.getText().toString());
                                                    }
                                                    adapter.notifyDataSetChanged();
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    //do nothing
                                                }
                                            }).show();
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Do nothing.
                            }
                        }).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int position, long arg3) {
                if (points[position] != 0) {
                    points[position] = 0;
                    cands[position] = cands[position].substring(0, cands[position].length() - "VOTED: 0".length());
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        return rootView;
    }

    public boolean pointsAssigned(int pointsx) {
        for (int i = 0; i < points.length; i++)
            if (points[i] == pointsx)
                return true;
        return false;
    }

    public int pointsAssignedTo(int pointsx) {
        for (int i = 0; i < points.length; i++)
            if (points[i] == pointsx)
                return i;
        return -1;
    }

    public String getNotAssigned() {
        String out = "";
        boolean voted[] = new boolean[points.length];

        for (int i = 0; i < points.length; i++)
            voted[i] = false;

        for (int i = 0; i < points.length; i++)
            if (points[i] != 0)
                voted[points[i] - 1] = true;

        for (int i = 0; i < voted.length; i++)
            if (!voted[i])
                out += i + 1 + " ";
        return out;
    }

    public boolean doneAndData() {
        for (int i = 0; i < points.length; i++)
            if (points[i] == 0)
                return false;
        if (points.length <= 2)
            Data.setPointsAbt(points);
        else
            Data.setPointsSchul(points);
        return true;
    }

}
