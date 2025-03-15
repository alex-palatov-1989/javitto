package com.solar.academy.models.user;

import com.solar.academy.dao.IRelative;
import com.solar.academy.models.BaseID;
import lombok.Data;

import java.util.List;

@Data
public class Seller extends BaseID{

    boolean isMarket;

    @IRelative.ToList
    public List<BaseID> posts;

    @IRelative.ToList
    public List<BaseID> questions;

    @IRelative.ToList
    public List<BaseID> deals;
}
