import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Latex {
    Path pathBase = Path.of("src/test/java/app/test");
    List<String> variants = List.of("acceptance", "integration");
    Map<String, String> tanslate = Map.of(
        "acceptance", "Test de aceptación", "integration", "Test de integración",
        "basic", "Requisito básico", "advanced", "Requisito avanzado",
        "requirement", "R", "history", "H", "sub", "S", 
        "/", ", ", ".java", ""
    );

    public static void main(String[] args) {
        new Latex();
    }

    public Latex() {
        var paths = getPaths();
        Map<String, Character> letters = new HashMap<>();
        for (var i = 0; i < paths.size(); i++) {
            var path = paths.get(i);
            String variant = path.subpath(0, 1).toString();

            char letter = letters.getOrDefault(variant, 'A');
            letters.put(variant, (char) (letter + 1));

            var lines = getTests(path);
            var cmd = String.format("\\newcommand\\test%s%c{", capitalize(variant), letter);
            lines.add(0, cmd); lines.add("}"); lines.add("");
            cmd = String.join("\n", lines);
            System.out.println(cmd);
        }
    }

    public List<Path> getPaths() {
        var pathBaseVariant = pathBase.resolve(variants.get(0));
        try (var pathWalker = Files.walk(pathBaseVariant)) {
            return pathWalker.map(Path::toFile).filter(File::isFile).map(File::toPath).map(pathBaseVariant::relativize).sorted().toList();
        } catch (IOException ignored) {
            throw new RuntimeException(ignored);
        }
    }

    public List<String> getTests(Path path) {
        return variants.stream().map(v -> Path.of(v).resolve(path)).map(this::getTest).collect(Collectors.toList());
    }

    public String getTest(Path path) {
        List<String> lineas;
        int start = 0, end = 0;
        var name = transalte(path.toString());
        path = pathBase.resolve(path);

        try {
            lineas = Files.readAllLines(path);
        } catch (IOException ignored) {
            throw new RuntimeException(ignored);
        }
        for (var i = 0; i < lineas.size(); i++) {
            if (lineas.get(i).startsWith("public")) start = i + 1;
            else if (lineas.get(i).startsWith("}")) end = i + 1;
        }

        var head = String.format("El siguiente test está en el repositorio \\texttt{app} del proyecto y se encuentra en la ruta: \\begin{itemize}\\setlength{\\itemindent}{-7mm}\\item [\\faIcon{code-branch}]\\texttt{%s}\\end{itemize}", path);
        var body = String.format("\\lstinputlisting[language=java, breaklines=true, linerange=%s-%s, firstnumber=%s, caption={%s}]{%s}", start, end, start, name, path);
        return String.format("%s\n%s", head, body);
    }

    public String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public String transalte(String string) {
        string = string.toLowerCase();
        for (Map.Entry<String, String> entry : tanslate.entrySet()) {
            string = string.replace(entry.getKey(), entry.getValue());
        }
        return string;
    }
}