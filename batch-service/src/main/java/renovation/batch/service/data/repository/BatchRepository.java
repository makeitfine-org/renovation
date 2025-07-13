package renovation.batch.service.data.repository;

import org.springframework.stereotype.Repository;
import renovation.batch.service.config.model.UserModel;

import java.util.HashSet;
import java.util.Set;

@Repository
public class BatchRepository {
    private final Set<UserModel> results = new HashSet<>();

    public Set<UserModel> getResults() {
        return results;
    }

    public void add(UserModel newResult) {
        results.add(newResult);
    }

    public void add(Set<UserModel> newResults) {
        results.addAll(newResults);
    }

    public void clear() {
        results.clear();
    }
}
