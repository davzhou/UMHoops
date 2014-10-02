package com.example.umhoops;

public class PagerFunctions {

    public final static int PAGE_LINKS = 5;
    public final static String PAGER_CLASS = "Pager";

    public static int[] pageOptions(int currentPage, int lastPage) {
        int[] pages;
        if (lastPage <= PAGE_LINKS) {
            pages = new int[lastPage];
            for (int i = 0; i < lastPage; i++) {
                pages[i] = i+1;
            }
        } else {
            pages = new int[PAGE_LINKS];
            pages[0] = 1;
            pages[4] = lastPage;
            if (currentPage == 1) {
                pages[1] = currentPage + 2;
                pages[2] = currentPage + 3;
                pages[3] = currentPage + 4;
            } else if (currentPage == lastPage) {
                pages[1] = currentPage - 4;
                pages[2] = currentPage - 3;
                pages[3] = currentPage - 2;
            } else if (lastPage == 6) {
                pages[1] = 2;
                pages[2] = currentPage;
                pages[3] = 5;
                if (currentPage == 2) {
                    pages[2] = 4;
                } else if (currentPage == 5) {
                    pages[2] = 3;
                }
            } else if (currentPage - 2 <= 1) {
                pages[1] = currentPage;
                pages[2] = currentPage + 2;
                pages[3] = currentPage + 3;
            } else if (currentPage + 2 >= lastPage) {
                pages[1] = currentPage - 3;
                pages[2] = currentPage - 2;
                pages[3] = currentPage;
            } else {
                pages[1] = currentPage - 2;
                pages[2] = currentPage;
                pages[3] = currentPage + 2;
            }
            pages[0] = 1;
            pages[4] = lastPage;
        }

        return pages;
    }
}
