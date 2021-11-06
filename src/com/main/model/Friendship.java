package com.main.model;

import java.time.LocalDateTime;

public class Friendship extends Entity<Tuple<Long,Long>> {

    LocalDateTime date;

    public Friendship(Long u1, Long u2) {
        Tuple<Long,Long> id = new Tuple<>(u1,u2);
        this.setId(id);
        date = LocalDateTime.now();
    }
    public Friendship(LocalDateTime time){
        setDate(time);
    }
    public void setDate(LocalDateTime time){
        date = time;
    }
    /**
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }
    @Override
    public String toString(){
        return this.getId().getLeft() + " " + this.getId().getRight() +", " +
                this.getDate();
    }
}
