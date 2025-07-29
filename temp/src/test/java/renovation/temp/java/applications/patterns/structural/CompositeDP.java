/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.applications.patterns.structural;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CompositeDP {

    @AllArgsConstructor
    abstract static class FileSystemEntity {
        protected final String name;

        public abstract String details(String intent);
    }

    static class File extends FileSystemEntity {

        public File(String name) {
            super(name);
        }

        @Override
        public String details(String intent) {
            return name;
        }
    }

    static class Folder extends FileSystemEntity {
        private final List<FileSystemEntity> files = new ArrayList<>();

        public Folder(String name) {
            super(name);
        }

        public void addFile(FileSystemEntity file) {
            files.add(file);
        }

        public void removeFile(FileSystemEntity file) {
            files.remove(file);
        }

        @Override
        public String details(String intent) {
            return files.stream()
                    .map(f -> f.details(intent))
                    .collect(Collectors.joining(intent));
        }
    }

    @Test
    public void test() {

        // When
        var intent = ", ";

        Folder pics = spy(new Folder("pics"));
        FileSystemEntity picFile1 = spy(new File("pic1.jpg"));
        FileSystemEntity picFile2 = spy(new File("pic2.jpg"));
        pics.addFile(picFile1);
        pics.addFile(picFile2);

        Folder binaries = spy(new Folder("binaries"));
        FileSystemEntity binary1 = spy(new File("binary1.bin"));
        binaries.addFile(binary1);

        Folder all = spy(new Folder("all"));
        all.addFile(pics);
        all.addFile(binaries);

        //Then
        var details = all.details(intent);
        assertEquals("pic1.jpg, pic2.jpg, binary1.bin", details);

        verify(all, times(1)).details(intent);

        verify(pics, times(1)).details(intent);
        verify(picFile1, times(1)).details(intent);
        verify(picFile2, times(1)).details(intent);

        verify(binaries, times(1)).details(intent);
        verify(binary1, times(1)).details(intent);
    }
}
