package com.team42.NHPS.api.medication.ui.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMedicationRequestModel {
    private String name;
    private String description;
    private String activeChemical;
    private String dosage;
}
