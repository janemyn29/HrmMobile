package com.monstertechno.moderndashbord.Model;

import java.util.ArrayList;

public class PagingDependent {
    public ArrayList<Dependent> items;
    public int pageNumber;
    public int totalPages;
    public int totalCount;
    public boolean hasPreviousPage;
    public boolean hasNextPage;
}
