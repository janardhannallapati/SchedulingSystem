package com.makespace.schedulingsystem.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BlockedTime {
    private final int blockStartTimeInt;
    private final int blockEndTimeInt;

    public BlockedTime(String startTime, String endTime){
        blockStartTimeInt = Integer.parseInt(startTime.replace(":",""));
        blockEndTimeInt = Integer.parseInt(endTime.replace(":",""));
    }

    public BlockedTime(int startTime, int endTime){
        blockStartTimeInt = startTime;
        blockEndTimeInt = endTime;
    }
    /**
     * Time values should already be validated before calling this method
     * @return
     */
    public boolean overlapsWithBookingTime(String bookingStartTime, String bookingEndTime){
        int bookingStartTimeInt = Integer.parseInt(bookingStartTime.replace(":",""));
        int bookingEndTimeInt = Integer.parseInt(bookingEndTime.replace(":",""));
        List<Integer> bookingRangeList = IntStream.range(bookingStartTimeInt, bookingEndTimeInt).boxed().collect(Collectors.toList());
        List<Integer> blockedRangeList = IntStream.range(blockStartTimeInt, blockEndTimeInt).boxed().collect(Collectors.toList());

        Set<Integer> result = bookingRangeList.stream()
                               .distinct().filter(blockedRangeList::contains)
                .collect(Collectors.toSet());

        return !result.isEmpty();
    }
}
