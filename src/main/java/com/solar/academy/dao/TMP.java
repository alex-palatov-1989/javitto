package com.solar.academy.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.solar.academy.models.BaseID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TMP extends BaseID{

    @Getter @Setter
    public String  name = "alex"; 

    @IRelative.ToList
    public List<TMP> linked = new ArrayList<>();
}

