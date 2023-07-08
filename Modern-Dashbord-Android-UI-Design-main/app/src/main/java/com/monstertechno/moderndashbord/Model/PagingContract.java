package com.monstertechno.moderndashbord.Model;

import java.util.ArrayList;

public class PagingContract {
    public ArrayList<Contract> items;
    public int pageNumber;
    public int totalPages;
    public int totalCount;
    public boolean hasPreviousPage;
    public boolean hasNextPage;
}
