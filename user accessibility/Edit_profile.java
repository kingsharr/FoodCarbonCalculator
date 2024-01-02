package com.example.foodcarboncalculator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Edit_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Edit_profile extends Fragment {

    private Spinner spinnerGender;
    private Spinner spinnerDiet;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Edit_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Edit_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Edit_profile newInstance(String param1, String param2) {
        Edit_profile fragment = new Edit_profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Initialize the spinners
        Spinner spinnerGender = view.findViewById(R.id.spinner_gender);
        Spinner spinnerDiet = view.findViewById(R.id.spinner_diet);

        if (spinnerGender != null && spinnerDiet != null) {
            // Create adapters
            ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(requireContext(),
                    R.array.gender, android.R.layout.simple_spinner_item);
            ArrayAdapter<CharSequence> adapterDiet = ArrayAdapter.createFromResource(requireContext(),
                    R.array.diet, android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears
            adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterDiet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapters to the spinners
            spinnerGender.setAdapter(adapterGender);
            spinnerDiet.setAdapter(adapterDiet);
        } else {
            // Handle case where spinners are not found in the layout
            // Log an error or show a toast message
        }

        //button to cancel
        Button btnCancel = view.findViewById(R.id.BtnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_edit_profile_to_DestProfile);
            }
        });

        return view;
    }

    public void showConfirmationDialog(View view) {
        // Implement your confirmation dialog logic here
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to discard changes?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform actions to discard changes
                        discardChanges(view);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog if "No" is clicked
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void discardChanges(View view) {
        Toast.makeText(requireContext(), "Changes discarded", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(view).navigate(R.id.action_edit_profile_to_DestProfile);
    }

  
}