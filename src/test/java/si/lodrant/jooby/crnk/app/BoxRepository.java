package si.lodrant.jooby.crnk.app;

import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryBase;
import io.crnk.core.resource.list.ResourceList;

import java.util.HashMap;
import java.util.Map;

public class BoxRepository extends ResourceRepositoryBase<Box, String> {
    private Map<Long, Box> boxes = new HashMap<>();

    public BoxRepository() {
        super(Box.class);
        save(new Box(1L, "Box A"));
        save(new Box(2L, "Box B"));
        save(new Box(3L, "Box C"));
    }

    @Override
    public synchronized void delete(String id) {
        boxes.remove(id);
    }

    @Override
    public synchronized <S extends Box> S save(S Box) {
        boxes.put(Box.getId(), Box);
        return Box;
    }

    @Override
    public synchronized ResourceList<Box> findAll(QuerySpec querySpec) {
        return querySpec.apply(boxes.values());
    }
}