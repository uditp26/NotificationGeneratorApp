package com.example.remotenotifier;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "senderDB")
public class SenderEntity {
    @PrimaryKey @NonNull
    public String sid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "choice")
    public String choice;

    @ColumnInfo(name = "rid")
    public String rid;

}
