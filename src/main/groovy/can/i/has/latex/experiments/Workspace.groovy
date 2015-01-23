package can.i.has.latex.experiments

import groovy.transform.Canonical

@Canonical
class Workspace {
    final File root

    final File resultsDir
    final File renderDir
    final File rawDir

    Workspace(File root) {
        this.root = root
        resultsDir = new File(root, "results")
        renderDir = new File(root, "render")
        rawDir = new File(root, "raw")
    }


    File resultFile(String... path) {
        resolve(resultsDir, path)
    }

    File renderFile(String... path) {
        resolve(renderDir, path)
    }

    File rawFile(String... path) {
        resolve(rawDir, path)
    }

    static File resolve(File root, String[] path) {
        File out = root
        path.each {
            out = new File(out, it)
        }
        out
    }

    @Singleton
    static class Manager {

        static protected ThreadLocal<Workspace> activeWorkspace

        static Workspace getActiveWorkspace() {
            def out = activeWorkspace.get()
            assert out != null        // todo: dedicated exception
            out
        }

        static {
            activeWorkspace = new ThreadLocal<>()
            activeWorkspace.set(null)
        }

        public <T> T withWorkspace(Workspace workspace, Closure<T> closure) {
            Workspace oldWorkspace = activeWorkspace.get()
            try {
                activeWorkspace.set(workspace)
                return closure()
            } finally {
                activeWorkspace.set(oldWorkspace)
            }
        }
    }
}
