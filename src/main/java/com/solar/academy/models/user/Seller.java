package com.solar.academy.models.user;

import com.solar.academy.dao.IRelative;
import com.solar.academy.models.BaseID;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class Seller extends BaseID{

    boolean isMarket;

    @IRelative.ToList
    public BaseID[] posts;    

    @IRelative.ToList
    public BaseID[] questions;

    @IRelative.ToList
    public BaseID[] deals;
}
