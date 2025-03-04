package com.solar.academy.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.solar.academy.models.BaseID;

@Getter @Setter 
@NoArgsConstructor
public class TMP extends BaseID{  
      
    public String  name = "alex"; 

    @IRelative.ToList
    public List<TMP> linked = new ArrayList<>();
}

