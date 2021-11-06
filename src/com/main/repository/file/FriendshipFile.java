package com.main.repository.file;

import com.main.model.Friendship;
import com.main.model.Tuple;
import com.main.model.validators.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipFile extends AbstractFileRepository<Tuple<Long,Long>, Friendship>{

    public FriendshipFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship pr = new Friendship(LocalDateTime.parse(attributes.get(2)));
        Long id1 = Long.parseLong(attributes.get(0));
        Long id2 = Long.parseLong(attributes.get(1));
        pr.setId(new Tuple<>(id1,id2));
        return pr;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId().getLeft() + ";" + entity.getId().getRight() + ";" +
                entity.getDate().toString();
    }
}
