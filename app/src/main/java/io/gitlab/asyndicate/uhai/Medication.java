package io.gitlab.asyndicate.uhai;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Medication extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView list = (RecyclerView) view.findViewById(R.id.list);
        final MenuAdapter adapter = new MenuAdapter();
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        MenuItem menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_TITLE);
        menuItem.setPrimaryText("Medication Info");
        adapter.add(menuItem);

        menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_DOUBLE_LINE);
        menuItem.setPrimaryText("Panadol");
        menuItem.setSecondaryText("Tap to edit");
        final MenuItem finalMenuItem = menuItem;
        menuItem.setAction(new PayloadRunnable() {
            @Override
            public Object run(Object result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter a drug");
                builder.setNegativeButton("Dismiss", null);
                View view = getActivity().getLayoutInflater().inflate(R.layout.input_text, null);
                final TextView text = (TextView) view.findViewById(R.id.input_text);
                text.setHint("Name");
                builder.setView(view);
                builder.setPositiveButton("Use", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = text.getText().toString().trim();
                        ((MenuItem) finalMenuItem).setPrimaryText(name);
//                        Settings.getInstance().putString("name", name);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.create().show();
                return null;
            }
        });

        adapter.add(menuItem);

        menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_DESCRIPTION);
        menuItem.setPrimaryText("Dosage and details");
        adapter.add(menuItem);

        menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_ONE_LINE);
        menuItem.setPrimaryText("Frequency");
        menuItem.setSecondaryText("Tap to edit");
        final MenuItem finalMenuItem1 = menuItem;
        menuItem.setAction(new PayloadRunnable() {
            @Override
            public Object run(Object result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Frequency");
                builder.setCancelable(false);
                View text = getActivity().getLayoutInflater().inflate(R.layout.spinner, null);
                builder.setView(text);

                final Spinner timing = (Spinner) text.findViewById(R.id.duration);
                String list[] = {"Once",
                        "Twice",
                        "Thrice"};

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                timing.setAdapter(dataAdapter);
                timing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        int list[] = {1, 2, 3};
                        int currentTiming = list[i];
                        finalMenuItem1.setSecondaryText((String) adapterView.getItemAtPosition(i));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                builder.setPositiveButton("USE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
                return null;
            }
        });
        adapter.add(menuItem);

        menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_CHECK);
        menuItem.setPrimaryText("Reminder notifications");

        adapter.add(menuItem);

        menuItem = new MenuItem();
        menuItem.setPrimaryText("Refill Reminder");
        menuItem.setType(MenuItem.TYPES.SECTION_DESCRIPTION);
        adapter.add(menuItem);

        menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_CHECK);
        menuItem.setPrimaryText("Enable this reminder?");
        adapter.add(menuItem);

        menuItem = new MenuItem();

        menuItem.setPrimaryText("How many drugs do i have?");
        menuItem.setSecondaryText("0");
        menuItem.setType(MenuItem.TYPES.SECTION_ONE_LINE);
        final MenuItem finalMenuItem2 = menuItem;
        menuItem.setAction(new PayloadRunnable() {
            @Override
            public Object run(Object result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter number");
                builder.setNegativeButton("Dismiss", null);
                View view = getActivity().getLayoutInflater().inflate(R.layout.input_text, null);
                final TextView text = (TextView) view.findViewById(R.id.input_text);
                text.setHint("number");
                builder.setView(view);
                builder.setPositiveButton("Use", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = text.getText().toString().trim();
                        finalMenuItem2.setSecondaryText(name);
//                        Settings.getInstance().putString("name", name);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.create().show();
                return null;
            }
        });

        adapter.add(menuItem);

        menuItem = new MenuItem();

        menuItem.setPrimaryText("Notify me when below?");
        menuItem.setSecondaryText("0");
        menuItem.setType(MenuItem.TYPES.SECTION_ONE_LINE);
        final MenuItem finalMenuItem3 = menuItem;
        menuItem.setAction(new PayloadRunnable() {
            @Override
            public Object run(Object result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter number");
                builder.setNegativeButton("Dismiss", null);
                View view = getActivity().getLayoutInflater().inflate(R.layout.input_text, null);
                final TextView text = (TextView) view.findViewById(R.id.input_text);
                text.setHint("number");
                builder.setView(view);
                builder.setPositiveButton("Use", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = text.getText().toString().trim();
                        finalMenuItem3.setSecondaryText(name);
//                        Settings.getInstance().putString("name", name);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.create().show();
                return null;
            }
        });
        adapter.add(menuItem);
    }
}
