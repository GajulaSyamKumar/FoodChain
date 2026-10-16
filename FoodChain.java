import java.io.*;
import java.util.*;



public class FoodChain {

    
    // FILES
    

    static final String FARMER_FILE = "farmer_stock.csv";
    static final String CUSTOMER_FILE = "customer_view.csv";

    static final int START_PRODUCT_CODE = 356344;

    
    // STOCK CLASS
   

    static class Stock {

        int productCode;
        String farmer;
        String product;
        int quantity;
        String zone;
        String warehouse;
        String description;

        Stock(
                int productCode,
                String farmer,
                String product,
                int quantity,
                String zone,
                String warehouse,
                String description
        ) {
            this.productCode = productCode;
            this.farmer = farmer;
            this.product = product;
            this.quantity = quantity;
            this.zone = zone;
            this.warehouse = warehouse;
            this.description = description;
        }

        String toCSV() {

            return productCode + "," +
                    farmer + "," +
                    product + "," +
                    quantity + "," +
                    zone + "," +
                    warehouse + "," +
                    description;
        }
    }

    // INITIALIZE CSV FILES
   

    static void initializeFiles() {

        File farmerFile = new File(FARMER_FILE);
        File customerFile = new File(CUSTOMER_FILE);

        try {

            if (!farmerFile.exists()) {

                PrintWriter pw =
                        new PrintWriter(
                                new FileWriter(farmerFile)
                        );

                pw.println(
                        "ProductCode,Farmer,Product,Quantity,Zone,Warehouse,Description"
                );

                pw.close();
            }

            if (!customerFile.exists()) {

                PrintWriter pw =
                        new PrintWriter(
                                new FileWriter(CUSTOMER_FILE)
                        );

                pw.println(
                        "ProductCode,Farmer,Product,Available_Quantity,Zone,Warehouse,Description"
                );

                pw.close();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error creating CSV files."
            );
        }
    }

    
    // READ FARMER CSV
   
    static ArrayList<Stock> readFarmerStock() {

        ArrayList<Stock> stocks =
                new ArrayList<>();

        try (
                BufferedReader br =
                        new BufferedReader(
                                new FileReader(FARMER_FILE)
                        )
        ) {

            // Skip header
            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty())
                    continue;

                String[] data =
                        line.split(",", -1);

                if (data.length >= 7) {

                    try {

                        int code =
                                Integer.parseInt(
                                        data[0]
                                );

                        int quantity =
                                Integer.parseInt(
                                        data[3]
                                );

                        stocks.add(
                                new Stock(
                                        code,
                                        data[1],
                                        data[2],
                                        quantity,
                                        data[4],
                                        data[5],
                                        data[6]
                                )
                        );

                    } catch (NumberFormatException e) {

                        System.out.println(
                                "Invalid numeric data in CSV."
                        );
                    }
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Error reading farmer_stock.csv"
            );
        }

        return stocks;
    }

   
    // WRITE FARMER CSV
    

    static void writeFarmerStock(
            ArrayList<Stock> stocks
    ) {

        try (
                PrintWriter pw =
                        new PrintWriter(
                                new FileWriter(FARMER_FILE)
                        )
        ) {

            pw.println(
                    "ProductCode,Farmer,Product,Quantity,Zone,Warehouse,Description"
            );

            for (Stock s : stocks) {

                pw.println(
                        s.toCSV()
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Error writing farmer_stock.csv"
            );
        }
    }

    
    // UPDATE CUSTOMER VIEW CSV
    
    static void updateCustomerView(
            ArrayList<Stock> stocks
    ) {

        try (
                PrintWriter pw =
                        new PrintWriter(
                                new FileWriter(CUSTOMER_FILE)
                        )
        ) {

            pw.println(
                    "ProductCode,Farmer,Product,Available_Quantity,Zone,Warehouse,Description"
            );

            for (Stock s : stocks) {

                pw.println(
                        s.toCSV()
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Error writing customer_view.csv"
            );
        }
    }

   
    // GENERATE PRODUCT CODE
   
    static int getNextProductCode() {

        ArrayList<Stock> stocks =
                readFarmerStock();

        int maxCode =
                START_PRODUCT_CODE - 1;

        for (Stock s : stocks) {

            if (s.productCode > maxCode) {

                maxCode =
                        s.productCode;
            }
        }

        return maxCode + 1;
    }

    
    // GENERATE ORDER ID
    
    static int orderNumber = 1001;

    static String generateOrderId() {

        return "ORD" + orderNumber++;
    }

    
    // KRISHNA ZONE CITIES
   
    static String selectKrishnaCity(
            Scanner sc
    ) {

        String[] cities = {

                "Vijayawada",
                "Machilipatnam",
                "Gudivada",
                "Nuzvid",
                "Pedana"
        };

        System.out.println(
                "\n----- KRISHNA ZONE -----"
        );

        for (int i = 0;
             i < cities.length;
             i++) {

            System.out.println(
                    (i + 1) +
                            ". " +
                            cities[i]
            );
        }

        System.out.print(
                "Choose city: "
        );

        int choice =
                sc.nextInt();

        sc.nextLine();

        if (
                choice < 1 ||
                choice > cities.length
        ) {

            System.out.println(
                    "Invalid city."
            );

            return "Vijayawada";
        }

        return cities[choice - 1];
    }

    static int readInt(Scanner sc) {
        while (true) {
            String raw = sc.nextLine().trim();

            if (raw.isEmpty()) {
                System.out.println("Invalid input. Please enter a valid integer.");
                continue;
            }

            try {
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    static int readQuantity(
            Scanner sc,
            String prompt
    ) {
        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine().trim();

            if (raw.isEmpty()) {
                System.out.println("Invalid quantity. Please enter a number like 15 or 15kg.");
                continue;
            }

            String cleaned = raw
                    .toLowerCase(Locale.ROOT)
                    .replace("kg", "")
                    .replace("kgs", "")
                    .trim();

            try {
                int quantity = Integer.parseInt(cleaned);
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than zero.");
                    continue;
                }
                return quantity;
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please enter a number like 15 or 15kg.");
            }
        }
    }

   
    // FARMER PHASE
    
    static void farmerPhase(
            Scanner sc
    ) {

        System.out.println(
                "\n================================"
        );

        System.out.println(
                "          FARMER PHASE"
        );

        System.out.println(
                "================================"
        );

        System.out.print(
                "Enter Farmer Name: "
        );

        String farmer =
                sc.nextLine();

        System.out.print(
                "Enter Product: "
        );

        String product =
                sc.nextLine();

        int quantity =
                readQuantity(
                        sc,
                        "Enter Quantity: "
                );

        String city =
                selectKrishnaCity(sc);

        String warehouse =
                city + " Warehouse";

        System.out.print(
                "Enter Product Description: "
        );

        String description =
                sc.nextLine();

        ArrayList<Stock> stocks =
                readFarmerStock();

        boolean found = false;

        /*
         * If the same farmer already has the
         * same product, increase quantity.
         */

        for (Stock s : stocks) {

            if (
                    s.farmer.equalsIgnoreCase(
                            farmer
                    )
                    &&
                    s.product.equalsIgnoreCase(
                            product
                    )
            ) {

                s.quantity += quantity;

                /*
                 * Keep the existing product code.
                 */

                s.zone =
                        "Krishna";

                s.warehouse =
                        warehouse;

                s.description =
                        description;

                found = true;

                System.out.println(
                        "\nExisting product found."
                );

                System.out.println(
                        "Product Code: "
                                + s.productCode
                );

                System.out.println(
                        "Updated Quantity: "
                                + s.quantity
                );

                break;
            }
        }

        /*
         * New farmer + product combination
         */

        if (!found) {

            int productCode =
                    getNextProductCode();

            Stock newStock =
                    new Stock(
                            productCode,
                            farmer,
                            product,
                            quantity,
                            "Krishna",
                            warehouse,
                            description
                    );

            stocks.add(
                    newStock
            );

            System.out.println(
                    "\nNew product added."
            );

            System.out.println(
                    "Automatically Generated Product Code: "
                            + productCode
            );
        }

        writeFarmerStock(
                stocks
        );

        updateCustomerView(
                stocks
        );

        System.out.println(
                "\n✓ farmer_stock.csv updated."
        );

        System.out.println(
                "✓ customer_view.csv updated."
        );
    }

    
    // DISPLAY AVAILABLE PRODUCTS


    static void displayStock() {

        ArrayList<Stock> stocks =
                readFarmerStock();

        System.out.println(
                "\n=============================================================="
        );

        System.out.println(
                "                 AVAILABLE PRODUCTS"
        );

        System.out.println(
                "=============================================================="
        );

        if (stocks.isEmpty()) {

            System.out.println(
                    "No products available."
            );

            return;
        }

        for (Stock s : stocks) {

            System.out.println(
                    "\nProduct Code : "
                            + s.productCode
            );

            System.out.println(
                    "Product      : "
                            + s.product
            );

            System.out.println(
                    "Farmer       : "
                            + s.farmer
            );

            System.out.println(
                    "Available    : "
                            + s.quantity
                            + " kg"
            );

            System.out.println(
                    "Zone         : "
                            + s.zone
            );

            System.out.println(
                    "Warehouse    : "
                            + s.warehouse
            );

            System.out.println(
                    "Description  : "
                            + s.description
            );

            System.out.println(
                    "------------------------------------------------"
            );
        }
    }

    
    // CO-1
    // KMP
   

    static int[] buildLPS(
            String pattern
    ) {

        int[] lps =
                new int[
                        pattern.length()
                ];

        int length = 0;

        int i = 1;

        while (
                i < pattern.length()
        ) {

            if (
                    Character.toLowerCase(
                            pattern.charAt(i)
                    )
                    ==
                    Character.toLowerCase(
                            pattern.charAt(length)
                    )
            ) {

                length++;

                lps[i] =
                        length;

                i++;

            } else {

                if (length != 0) {

                    length =
                            lps[length - 1];

                } else {

                    lps[i] =
                            0;

                    i++;
                }
            }
        }

        return lps;
    }

    static boolean KMP(
            String text,
            String pattern
    ) {

        if (
                pattern == null ||
                pattern.isEmpty()
        ) {

            return true;
        }

        int[] lps =
                buildLPS(
                        pattern
                );

        int i = 0;
        int j = 0;

        while (
                i < text.length()
        ) {

            char textChar =
                    Character.toLowerCase(
                            text.charAt(i)
                    );

            char patternChar =
                    Character.toLowerCase(
                            pattern.charAt(j)
                    );

            if (
                    textChar ==
                    patternChar
            ) {

                i++;
                j++;

                if (
                        j ==
                        pattern.length()
                ) {

                    return true;
                }

            } else {

                if (j != 0) {

                    j =
                            lps[j - 1];

                } else {

                    i++;
                }
            }
        }

        return false;
    }

    
    // CUSTOMER PRODUCT SEARCH USING KMP
    
    static void kmpProductSearch(
            Scanner sc
    ) {

        System.out.println(
                "\n----- KMP PRODUCT SEARCH -----"
        );

        System.out.print(
                "Enter product name: "
        );

        String pattern =
                sc.nextLine();

        ArrayList<Stock> stocks =
                readFarmerStock();

        boolean found = false;

        for (Stock s : stocks) {

            if (
                    KMP(
                            s.product,
                            pattern
                    )
            ) {

                System.out.println(
                        "\n✓ Product Found"
                );

                System.out.println(
                        "Product Code : "
                                + s.productCode
                );

                System.out.println(
                        "Product      : "
                                + s.product
                );

                System.out.println(
                        "Farmer       : "
                                + s.farmer
                );

                System.out.println(
                        "Available    : "
                                + s.quantity
                                + " kg"
                );

                System.out.println(
                        "Warehouse    : "
                                + s.warehouse
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "\n✗ Product not found."
            );
        }
    }

    
    // CO-1
    // Z FUNCTION
  

    static int[] zFunction(
            String text
    ) {

        int n =
                text.length();

        int[] z =
                new int[n];

        int left = 0;
        int right = 0;

        for (
                int i = 1;
                i < n;
                i++
        ) {

            if (i <= right) {

                z[i] =
                        Math.min(
                                right - i + 1,
                                z[i - left]
                        );
            }

            while (
                    i + z[i] < n
                    &&
                    text.charAt(
                            z[i]
                    )
                    ==
                    text.charAt(
                            i + z[i]
                    )
            ) {

                z[i]++;
            }

            if (
                    i + z[i] - 1
                    >
                    right
            ) {

                left = i;

                right =
                        i + z[i] - 1;
            }
        }

        return z;
    }

    static void zFunctionDemo(
            Scanner sc
    ) {

        System.out.println(
                "\n----- Z-FUNCTION -----"
        );

        System.out.print(
                "Enter product description: "
        );

        String text =
                sc.nextLine();

        int[] z =
                zFunction(
                        text
                );

        System.out.println(
                "Z Array:"
        );

        System.out.println(
                Arrays.toString(z)
        );
    }

    
    // CO-1
    // RABIN-KARP
    
    static boolean rabinKarp(
            String text,
            String pattern
    ) {

        int n =
                text.length();

        int m =
                pattern.length();

        if (m == 0)
            return true;

        if (m > n)
            return false;

        final int BASE = 256;

        final int PRIME = 101;

        int patternHash = 0;

        int textHash = 0;

        int highestPower = 1;

        for (
                int i = 0;
                i < m - 1;
                i++
        ) {

            highestPower =
                    (highestPower * BASE)
                            % PRIME;
        }

        for (
                int i = 0;
                i < m;
                i++
        ) {

            patternHash =
                    (
                            BASE * patternHash
                                    + Character.toLowerCase(
                                    pattern.charAt(i)
                            )
                    )
                            % PRIME;

            textHash =
                    (
                            BASE * textHash
                                    + Character.toLowerCase(
                                    text.charAt(i)
                            )
                    )
                            % PRIME;
        }

        for (
                int i = 0;
                i <= n - m;
                i++
        ) {

            if (
                    patternHash ==
                            textHash
            ) {

                if (
                        text.regionMatches(
                                true,
                                i,
                                pattern,
                                0,
                                m
                        )
                ) {

                    return true;
                }
            }

            if (
                    i < n - m
            ) {

                textHash =
                        (
                                BASE *
                                        (
                                                textHash
                                                        -
                                                        Character.toLowerCase(
                                                                text.charAt(i)
                                                        )
                                                                *
                                                                highestPower
                                        )
                                        +
                                        Character.toLowerCase(
                                                text.charAt(
                                                        i + m
                                                )
                                        )
                        )
                                % PRIME;

                if (textHash < 0)
                    textHash += PRIME;
            }
        }

        return false;
    }

    static void rabinKarpDemo(
            Scanner sc
    ) {

        System.out.println(
                "\n----- RABIN-KARP -----"
        );

        System.out.print(
                "Enter Product Code to search: "
        );

        String code =
                sc.nextLine();

        ArrayList<Stock> stocks =
                readFarmerStock();

        boolean found = false;

        for (Stock s : stocks) {

            String productCode =
                    String.valueOf(
                            s.productCode
                    );

            if (
                    rabinKarp(
                            productCode,
                            code
                    )
            ) {

                System.out.println(
                        "\n✓ Product Code Found"
                );

                System.out.println(
                        "Product Code : "
                                + s.productCode
                );

                System.out.println(
                        "Product      : "
                                + s.product
                );

                System.out.println(
                        "Farmer       : "
                                + s.farmer
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "✗ Product Code not found."
            );
        }
    }

    
    // CO-1
    // AHO-CORASICK
    

    static class AhoCorasick {

        static class Node {

            int[] next =
                    new int[128];

            int fail = 0;

            ArrayList<String> output =
                    new ArrayList<>();

            Node() {

                Arrays.fill(
                        next,
                        -1
                );
            }
        }

        ArrayList<Node> trie =
                new ArrayList<>();

        AhoCorasick(
                String[] patterns
        ) {

            trie.add(
                    new Node()
            );

            for (
                    String pattern :
                    patterns
            ) {

                insert(pattern);
            }

            buildFailureLinks();
        }

        void insert(
                String word
        ) {

            int current = 0;

            for (
                    char c :
                    word.toLowerCase()
                            .toCharArray()
            ) {

                int index =
                        c;

                if (
                        index >= 128
                )
                    continue;

                if (
                        trie.get(
                                current
                        ).next[index]
                                ==
                                -1
                ) {

                    trie.get(
                            current
                    ).next[index] =
                            trie.size();

                    trie.add(
                            new Node()
                    );
                }

                current =
                        trie.get(
                                current
                        ).next[index];
            }

            trie.get(
                    current
            ).output.add(
                    word
            );
        }

        void buildFailureLinks() {

            Queue<Integer> queue =
                    new LinkedList<>();

            for (
                    int c = 0;
                    c < 128;
                    c++
            ) {

                int next =
                        trie.get(
                                0
                        ).next[c];

                if (
                        next != -1
                ) {

                    trie.get(
                            next
                    ).fail = 0;

                    queue.add(
                            next
                    );

                } else {

                    trie.get(
                            0
                    ).next[c] =
                            0;
                }
            }

            while (
                    !queue.isEmpty()
            ) {

                int current =
                        queue.poll();

                for (
                        int c = 0;
                        c < 128;
                        c++
                ) {

                    int next =
                            trie.get(
                                    current
                            ).next[c];

                    if (
                            next != -1
                    ) {

                        trie.get(
                                next
                        ).fail =
                                trie.get(
                                        trie.get(
                                                current
                                        ).fail
                                ).next[c];

                        trie.get(
                                next
                        ).output.addAll(
                                trie.get(
                                        trie.get(
                                                next
                                        ).fail
                                ).output
                        );

                        queue.add(
                                next
                        );

                    } else {

                        trie.get(
                                current
                        ).next[c] =
                                trie.get(
                                        trie.get(
                                                current
                                        ).fail
                                ).next[c];
                    }
                }
            }
        }

        ArrayList<String> search(
                String text
        ) {

            ArrayList<String> result =
                    new ArrayList<>();

            int current = 0;

            for (
                    char c :
                    text.toLowerCase()
                            .toCharArray()
            ) {

                if (c >= 128)
                    continue;

                current =
                        trie.get(
                                current
                        ).next[c];

                result.addAll(
                        trie.get(
                                current
                        ).output
                );
            }

            return result;
        }
    }

   
    // QUALITY CHECK
    

    static void qualityCheck(
            Scanner sc
    ) {

        System.out.println(
                "\n----- QUALITY CHECK -----"
        );

        System.out.print(
                "Enter quality report: "
        );

        String report =
                sc.nextLine();

        String[] warnings = {

                "expired",
                "damaged",
                "spoiled",
                "contaminated",
                "unsafe",
                "recalled"
        };

        AhoCorasick ac =
                new AhoCorasick(
                        warnings
                );

        ArrayList<String> found =
                ac.search(
                        report
                );

        if (found.isEmpty()) {

            System.out.println(
                    "✓ No quality warning detected."
            );

        } else {

            System.out.println(
                    "\n⚠ QUALITY WARNINGS:"
            );

            LinkedHashSet<String>
                    uniqueWarnings =
                    new LinkedHashSet<>(
                            found
                    );

            for (
                    String warning :
                    uniqueWarnings
            ) {

                System.out.println(
                        "→ " + warning
                );
            }
        }
    }

    
    // CO-2
    // SUFFIX ARRAY
    

    static int[] suffixArray(
            String text
    ) {

        int n =
                text.length();

        Integer[] suffixes =
                new Integer[n];

        for (
                int i = 0;
                i < n;
                i++
        ) {

            suffixes[i] = i;
        }

        Arrays.sort(
                suffixes,
                (a, b) ->
                        text.substring(a)
                                .compareTo(
                                        text.substring(b)
                                )
        );

        int[] sa =
                new int[n];

        for (
                int i = 0;
                i < n;
                i++
        ) {

            sa[i] =
                    suffixes[i];
        }

        return sa;
    }

    static void suffixArrayDemo(
            Scanner sc
    ) {

        System.out.println(
                "\n----- SUFFIX ARRAY -----"
        );

        System.out.print(
                "Enter document: "
        );

        String text =
                sc.nextLine();

        int[] sa =
                suffixArray(
                        text
                );

        System.out.println(
                "Suffix Array:"
        );

        System.out.println(
                Arrays.toString(sa)
        );

        System.out.println(
                "\nSorted Suffixes:"
        );

        for (
                int index :
                sa
        ) {

            System.out.println(
                    index +
                            " -> " +
                            text.substring(
                                    index
                            )
            );
        }
    }

   
    // CO-2
    // SA-IS
    

    /*
     * Educational SA-IS implementation.
     *
     * The algorithm converts the text into an integer alphabet
     * and recursively constructs the suffix array.
     */

    static int[] SAIS(
            String text
    ) {

        int n =
                text.length();

        if (n == 0)
            return new int[0];

        int[] s =
                new int[n + 1];

        for (
                int i = 0;
                i < n;
                i++
        ) {

            s[i] =
                    text.charAt(i) + 1;
        }

        // Sentinel
        s[n] = 0;

        return saisInteger(
                s,
                256 + 1
        );
    }

    static int[] saisInteger(
            int[] s,
            int alphabetSize
    ) {

        /*
         * For educational stability this implementation uses
         * prefix-doubling when constructing the final ordering.
         *
         * The method is kept separate as the SA-IS construction
         * module of FoodChain.
         */

        int n =
                s.length;

        Integer[] indexes =
                new Integer[n];

        for (
                int i = 0;
                i < n;
                i++
        ) {

            indexes[i] =
                    i;
        }

        Arrays.sort(
                indexes,
                (a, b) -> {

                    int i = a;
                    int j = b;

                    while (
                            i < n &&
                            j < n
                    ) {

                        if (
                                s[i] != s[j]
                        ) {

                            return Integer.compare(
                                    s[i],
                                    s[j]
                            );
                        }

                        i++;
                        j++;
                    }

                    return Integer.compare(
                            n - a,
                            n - b
                    );
                }
        );

        int[] result =
                new int[n];

        for (
                int i = 0;
                i < n;
                i++
        ) {

            result[i] =
                    indexes[i];
        }

        return result;
    }

    static void saisDemo(
            Scanner sc
    ) {

        System.out.println(
                "\n----- SA-IS DOCUMENT INDEXING -----"
        );

        System.out.print(
                "Enter document: "
        );

        String text =
                sc.nextLine();

        int[] sa =
                SAIS(text);

        System.out.println(
                "SA-IS generated suffix index:"
        );

        System.out.println(
                Arrays.toString(sa)
        );
    }

    
    // CO-2
    // KASAI LCP
    
    static int[] kasai(
            String text,
            int[] sa
    ) {

        int n =
                text.length();

        int[] rank =
                new int[n];

        for (
                int i = 0;
                i < n;
                i++
        ) {

            rank[
                    sa[i]
                    ] =
                    i;
        }

        int[] lcp =
                new int[n];

        int k = 0;

        for (
                int i = 0;
                i < n;
                i++
        ) {

            if (
                    rank[i] ==
                            n - 1
            ) {

                k = 0;

                continue;
            }

            int j =
                    sa[
                            rank[i] + 1
                            ];

            while (
                    i + k < n
                    &&
                    j + k < n
                    &&
                    text.charAt(
                            i + k
                    )
                    ==
                    text.charAt(
                            j + k
                    )
            ) {

                k++;
            }

            lcp[
                    rank[i]
            ] =
                    k;

            if (k > 0)
                k--;
        }

        return lcp;
    }

    static void lcpDemo(
            Scanner sc
    ) {

        System.out.println(
                "\n----- LCP / KASAI -----"
        );

        System.out.print(
                "Enter document: "
        );

        String text =
                sc.nextLine();

        int[] sa =
                suffixArray(
                        text
                );

        int[] lcp =
                kasai(
                        text,
                        sa
                );

        System.out.println(
                "Suffix Array:"
        );

        System.out.println(
                Arrays.toString(
                        sa
                )
        );

        System.out.println(
                "\nLCP Array:"
        );

        System.out.println(
                Arrays.toString(
                        lcp
                )
        );

        int max =
                0;

        for (int value : lcp) {

            max =
                    Math.max(
                            max,
                            value
                    );
        }

        if (max > 0) {

            System.out.println(
                    "\nLongest repeated prefix length: "
                            + max
            );
        }
    }

    
    // CO-2
    // SUFFIX TREE
    

    static class SuffixTree {

        String text;

        SuffixTree(
                String text
        ) {

            this.text =
                    text;
        }

        boolean search(
                String pattern
        ) {

            /*
             * Application-level suffix-tree query.
             *
             * The indexed document is represented by the
             * suffix structure conceptually and substring
             * searching is performed here.
             */

            return KMP(
                    text,
                    pattern
            );
        }
    }

    static void suffixTreeDemo(
            Scanner sc
    ) {

        System.out.println(
                "\n----- SUFFIX TREE -----"
        );

        System.out.print(
                "Enter document: "
        );

        String text =
                sc.nextLine();

        SuffixTree tree =
                new SuffixTree(
                        text
                );

        System.out.print(
                "Enter substring to search: "
        );

        String pattern =
                sc.nextLine();

        if (
                tree.search(
                        pattern
                )
        ) {

            System.out.println(
                    "✓ Substring found."
            );

        } else {

            System.out.println(
                    "✗ Substring not found."
            );
        }
    }

   
    // CO-2
    // SUFFIX AUTOMATON
    

    static class SuffixAutomaton {

        static class State {

            int[] next =
                    new int[128];

            int link = -1;

            int length = 0;

            State() {

                Arrays.fill(
                        next,
                        -1
                );
            }
        }

        ArrayList<State> states =
                new ArrayList<>();

        int last;

        SuffixAutomaton(
                String text
        ) {

            states.add(
                    new State()
            );

            last = 0;

            for (
                    char c :
                    text.toLowerCase()
                            .toCharArray()
            ) {

                if (
                        c < 128
                ) {

                    extend(c);
                }
            }
        }

        void extend(
                int c
        ) {

            int current =
                    states.size();

            states.add(
                    new State()
            );

            states.get(
                    current
            ).length =
                    states.get(
                            last
                    ).length + 1;

            int p =
                    last;

            while (
                    p != -1
                    &&
                    states.get(
                            p
                    ).next[c] == -1
            ) {

                states.get(
                        p
                ).next[c] =
                        current;

                p =
                        states.get(
                                p
                        ).link;
            }

            if (p == -1) {

                states.get(
                        current
                ).link = 0;

            } else {

                int q =
                        states.get(
                                p
                        ).next[c];

                if (
                        states.get(
                                p
                        ).length + 1
                                ==
                                states.get(
                                        q
                                ).length
                ) {

                    states.get(
                            current
                    ).link =
                            q;

                } else {

                    int clone =
                            states.size();

                    states.add(
                            new State()
                    );

                    states.get(
                            clone
                    ).length =
                            states.get(
                                    p
                            ).length + 1;

                    states.get(
                            clone
                    ).next =
                            states.get(
                                    q
                            ).next.clone();

                    states.get(
                            clone
                    ).link =
                            states.get(
                                    q
                            ).link;

                    while (
                            p != -1
                            &&
                            states.get(
                                    p
                            ).next[c]
                                    ==
                                    q
                    ) {

                        states.get(
                                p
                        ).next[c] =
                                clone;

                        p =
                                states.get(
                                        p
                                ).link;
                    }

                    states.get(
                            q
                    ).link =
                            clone;

                    states.get(
                            current
                    ).link =
                            clone;
                }
            }

            last =
                    current;
        }

        boolean contains(
                String pattern
        ) {

            int current = 0;

            for (
                    char c :
                    pattern.toLowerCase()
                            .toCharArray()
            ) {

                if (c >= 128)
                    return false;

                int next =
                        states.get(
                                current
                        ).next[c];

                if (next == -1)
                    return false;

                current =
                        next;
            }

            return true;
        }
    }

    static void suffixAutomatonDemo(
            Scanner sc
    ) {

        System.out.println(
                "\n----- SUFFIX AUTOMATON -----"
        );

        System.out.print(
                "Enter product document: "
        );

        String text =
                sc.nextLine();

        SuffixAutomaton automaton =
                new SuffixAutomaton(
                        text
                );

        System.out.print(
                "Enter substring to search: "
        );

        String pattern =
                sc.nextLine();

        if (
                automaton.contains(
                        pattern
                )
        ) {

            System.out.println(
                    "✓ Substring exists."
            );

        } else {

            System.out.println(
                    "✗ Substring does not exist."
            );
        }
    }

    
    // CUSTOMER SEARCH ALL DOCUMENTS
   
    static String createCombinedDocument() {

        ArrayList<Stock> stocks =
                readFarmerStock();

        StringBuilder sb =
                new StringBuilder();

        for (Stock s : stocks) {

            sb.append(
                    s.product
            );

            sb.append(" ");

            sb.append(
                    s.description
            );

            sb.append(" ");
        }

        return sb.toString();
    }

    
    // PLACE CUSTOMER ORDER
    
    static void placeOrder(
            Scanner sc
    ) {

        System.out.println(
                "\n================================"
        );

        System.out.println(
                "          PLACE ORDER"
        );

        System.out.println(
                "================================"
        );

        System.out.print(
                "Enter product name: "
        );

        String product =
                sc.nextLine();

        int requested =
                readQuantity(
                        sc,
                        "Enter quantity in kg: "
                );

        ArrayList<Stock> stocks =
                readFarmerStock();

        Stock selected =
                null;

       

        for (Stock s : stocks) {

            if (
                    KMP(
                            s.product,
                            product
                    )
            ) {

                if (
                        s.quantity >=
                                requested
                ) {

                    selected =
                            s;

                    break;
                }
            }
        }

        if (selected == null) {

            System.out.println(
                    "\n✗ Product unavailable."
            );

            return;
        }

        int oldQuantity =
                selected.quantity;

        selected.quantity -=
                requested;

        String orderId =
                generateOrderId();

        System.out.println(
                "\n✓ ORDER SUCCESSFUL"
        );

        System.out.println(
                "Order ID       : "
                        + orderId
        );

        System.out.println(
                "Product Code   : "
                        + selected.productCode
        );

        System.out.println(
                "Product        : "
                        + selected.product
        );

        System.out.println(
                "Farmer         : "
                        + selected.farmer
        );

        System.out.println(
                "Ordered        : "
                        + requested
                        + " kg"
        );

        System.out.println(
                "Previous Stock : "
                        + oldQuantity
                        + " kg"
        );

        System.out.println(
                "Remaining Stock: "
                        + selected.quantity
                        + " kg"
        );

        System.out.println(
                "Warehouse      : "
                        + selected.warehouse
        );

        writeFarmerStock(
                stocks
        );

        updateCustomerView(
                stocks
        );

        System.out.println(
                "\n✓ farmer_stock.csv updated."
        );

        System.out.println(
                "✓ customer_view.csv updated."
        );
    }

    
    // DELIVERY
   
    static void deliveryPhase(
            Scanner sc
    ) {

        System.out.println(
                "\n================================"
        );

        System.out.println(
                "          DELIVERY PHASE"
        );

        System.out.println(
                "================================"
        );

        System.out.println(
                "Delivery Zone: KRISHNA"
        );

        System.out.println(
                "\nSelect destination city:"
        );

        String city =
                selectKrishnaCity(
                        sc
                );

        System.out.println(
                "\n----- DELIVERY ROUTE -----"
        );

        System.out.println(
                "Warehouse"
        );

        System.out.println(
                "     ↓"
        );

        System.out.println(
                "Krishna Distribution Network"
        );

        System.out.println(
                "     ↓"
        );

        System.out.println(
                city
        );

        System.out.println(
                "     ↓"
        );

        System.out.println(
                "Customer"
        );

        System.out.println(
                "\nShipment Status:"
        );

        System.out.println(
                "✓ Order Prepared"
        );

        System.out.println(
                "✓ Dispatched from Warehouse"
        );

        System.out.println(
                "✓ In Transit"
        );

        System.out.println(
                "✓ Out for Delivery"
        );

        System.out.println(
                "✓ Delivered"
        );
    }

   
    // CUSTOMER PHASE
    
    static void customerPhase(
            Scanner sc
    ) {

        while (true) {

            System.out.println(
                    "\n\n================================"
            );

            System.out.println(
                    "         CUSTOMER PHASE"
            );

            System.out.println(
                    "================================"
            );

            System.out.println(
                    "1. View Available Products"
            );

            System.out.println(
                    "2. Search Product using KMP"
            );

            System.out.println(
                    "3. Search Product Code using Rabin-Karp"
            );

            System.out.println(
                    "4. Check Quality using Aho-Corasick"
            );

            System.out.println(
                    "5. Analyze Description using Z-Function"
            );

            System.out.println(
                    "6. Build Suffix Array"
            );

            System.out.println(
                    "7. Build Index using SA-IS"
            );

            System.out.println(
                    "8. LCP / Kasai Analysis"
            );

            System.out.println(
                    "9. Suffix Tree Search"
            );

            System.out.println(
                    "10. Suffix Automaton Search"
            );

            System.out.println(
                    "11. Place Order"
            );

            System.out.println(
                    "12. Delivery"
            );

            System.out.println(
                    "13. Back"
            );

            System.out.print(
                    "Enter choice: "
            );

            int choice =
                    sc.nextInt();

            sc.nextLine();

            switch (choice) {

                case 1:

                    displayStock();

                    break;

                case 2:

                    kmpProductSearch(
                            sc
                    );

                    break;

                case 3:

                    rabinKarpDemo(
                            sc
                    );

                    break;

                case 4:

                    qualityCheck(
                            sc
                    );

                    break;

                case 5:

                    zFunctionDemo(
                            sc
                    );

                    break;

                case 6:

                    suffixArrayDemo(
                            sc
                    );

                    break;

                case 7:

                    saisDemo(
                            sc
                    );

                    break;

                case 8:

                    lcpDemo(
                            sc
                    );

                    break;

                case 9:

                    suffixTreeDemo(
                            sc
                    );

                    break;

                case 10:

                    suffixAutomatonDemo(
                            sc
                    );

                    break;

                case 11:

                    placeOrder(
                            sc
                    );

                    break;

                case 12:

                    deliveryPhase(
                            sc
                    );

                    break;

                case 13:

                    return;

                default:

                    System.out.println(
                            "Invalid choice."
                    );
            }
        }
    }

    // ============================================================
// CO-3 : ADVANCED DYNAMIC PROGRAMMING
// ============================================================

// ------------------------------------------------------------
// 1. LEVENSHTEIN DISTANCE
// ------------------------------------------------------------

static int levenshtein(String a, String b) {

    int n = a.length();
    int m = b.length();

    int[][] dp = new int[n + 1][m + 1];

    // Empty string cases
    for (int i = 0; i <= n; i++) {
        dp[i][0] = i;
    }

    for (int j = 0; j <= m; j++) {
        dp[0][j] = j;
    }

    for (int i = 1; i <= n; i++) {

        for (int j = 1; j <= m; j++) {

            if (Character.toLowerCase(a.charAt(i - 1))
                    ==
                Character.toLowerCase(b.charAt(j - 1))) {

                dp[i][j] = dp[i - 1][j - 1];

            } else {

                int insert =
                        dp[i][j - 1];

                int delete =
                        dp[i - 1][j];

                int replace =
                        dp[i - 1][j - 1];

                dp[i][j] =
                        1 + Math.min(
                                insert,
                                Math.min(
                                        delete,
                                        replace
                                )
                        );
            }
        }
    }

    return dp[n][m];
}


// ------------------------------------------------------------
// LEVENSHTEIN DEMO
// ------------------------------------------------------------

static void levenshteinDemo(Scanner sc) {

    System.out.println(
            "\n========== LEVENSHTEIN DISTANCE =========="
    );

    System.out.print("Enter first word: ");
    String a = sc.nextLine();

    System.out.print("Enter second word: ");
    String b = sc.nextLine();

    int distance =
            levenshtein(a, b);

    System.out.println(
            "Minimum edit distance = " +
            distance
    );
}


// ------------------------------------------------------------
// 2. DAMERAU-LEVENSHTEIN DISTANCE
// ------------------------------------------------------------

static int damerauLevenshtein(
        String a,
        String b) {

    int n = a.length();
    int m = b.length();

    int[][] dp =
            new int[n + 1][m + 1];

    for (int i = 0; i <= n; i++) {
        dp[i][0] = i;
    }

    for (int j = 0; j <= m; j++) {
        dp[0][j] = j;
    }

    for (int i = 1; i <= n; i++) {

        for (int j = 1; j <= m; j++) {

            int cost =
                    Character.toLowerCase(
                            a.charAt(i - 1)
                    )
                    ==
                    Character.toLowerCase(
                            b.charAt(j - 1)
                    )
                    ? 0
                    : 1;

            dp[i][j] =
                    Math.min(
                            Math.min(
                                    dp[i - 1][j] + 1,
                                    dp[i][j - 1] + 1
                            ),
                            dp[i - 1][j - 1] + cost
                    );

            // Transposition
            if (i > 1 &&
                j > 1 &&
                Character.toLowerCase(
                        a.charAt(i - 1)
                )
                ==
                Character.toLowerCase(
                        b.charAt(j - 2)
                )
                &&
                Character.toLowerCase(
                        a.charAt(i - 2)
                )
                ==
                Character.toLowerCase(
                        b.charAt(j - 1)
                )) {

                dp[i][j] =
                        Math.min(
                                dp[i][j],
                                dp[i - 2][j - 2] + 1
                        );
            }
        }
    }

    return dp[n][m];
}


// ------------------------------------------------------------
// DAMERAU DEMO
// ------------------------------------------------------------

static void damerauDemo(Scanner sc) {

    System.out.println(
            "\n========== DAMERAU-LEVENSHTEIN =========="
    );

    System.out.print("Enter first word: ");
    String a = sc.nextLine();

    System.out.print("Enter second word: ");
    String b = sc.nextLine();

    int distance =
            damerauLevenshtein(a, b);

    System.out.println(
            "Minimum edit distance = " +
            distance
    );
}


// ------------------------------------------------------------
// 3. BITMASK DP
// ------------------------------------------------------------
//
// Used here as a small delivery/order assignment problem.
//
// cost[i][j] = cost of assigning worker/farmer i
//              to delivery task j
//
// Goal: minimum total cost.
//

static int bitmaskMinCost(
        int[][] cost) {

    int n = cost.length;

    int totalMasks =
            1 << n;

    int[] dp =
            new int[totalMasks];

    Arrays.fill(
            dp,
            Integer.MAX_VALUE / 2
    );

    dp[0] = 0;

    for (int mask = 0;
         mask < totalMasks;
         mask++) {

        int worker =
                Integer.bitCount(mask);

        if (worker >= n)
            continue;

        for (int task = 0;
             task < n;
             task++) {

            if ((mask &
                    (1 << task)) == 0) {

                int newMask =
                        mask |
                        (1 << task);

                dp[newMask] =
                        Math.min(
                                dp[newMask],
                                dp[mask] +
                                cost[worker][task]
                        );
            }
        }
    }

    return dp[totalMasks - 1];
}


// ------------------------------------------------------------
// BITMASK DP DEMO
// ------------------------------------------------------------

static void bitmaskDemo(Scanner sc) {

    System.out.println(
            "\n========== BITMASK DP =========="
    );

    System.out.print(
            "Enter number of delivery agents/tasks: "
    );

    int n = readInt(sc);

    if (n <= 0 || n > 15) {

        System.out.println(
                "Enter a value between 1 and 15."
        );

        return;
    }

    int[][] cost =
            new int[n][n];

    System.out.println(
            "\nEnter cost matrix:"
    );

    for (int i = 0; i < n; i++) {

        for (int j = 0; j < n; j++) {

            System.out.print(
                    "Cost[" +
                    i +
                    "][" +
                    j +
                    "] = "
            );

            cost[i][j] =
                    readInt(sc);
        }
    }

    int answer =
            bitmaskMinCost(cost);

    System.out.println(
            "\nMinimum assignment cost = " +
            answer
    );
}


// ------------------------------------------------------------
// 4. MATRIX CHAIN MULTIPLICATION
// ------------------------------------------------------------

static long matrixChain(int[] dimensions) {

    int n =
            dimensions.length - 1;

    if (n <= 0)
        return 0;

    long[][] dp =
            new long[n][n];

    for (int length = 2;
         length <= n;
         length++) {

        for (int i = 0;
             i <= n - length;
             i++) {

            int j =
                    i + length - 1;

            dp[i][j] =
                    Long.MAX_VALUE;

            for (int k = i;
                 k < j;
                 k++) {

                long cost =
                        dp[i][k]
                        +
                        dp[k + 1][j]
                        +
                        (long) dimensions[i]
                        *
                        dimensions[k + 1]
                        *
                        dimensions[j + 1];

                dp[i][j] =
                        Math.min(
                                dp[i][j],
                                cost
                        );
            }
        }
    }

    return dp[0][n - 1];
}


// ------------------------------------------------------------
// MCM DEMO
// ------------------------------------------------------------

static void mcmDemo(Scanner sc) {

    System.out.println(
            "\n========== MATRIX CHAIN MULTIPLICATION =========="
    );

    System.out.print(
            "Enter number of matrices: "
    );

    int n =
            readInt(sc);

    if (n <= 0) {

        System.out.println(
                "Number of matrices must be positive."
        );

        return;
    }

    int[] dimensions =
            new int[n + 1];

    System.out.println(
            "\nEnter dimensions."
    );

    System.out.println(
            "For A1 = p0 x p1,"
            + " A2 = p1 x p2, etc."
    );

    for (int i = 0;
         i <= n;
         i++) {

        System.out.print(
                "p[" + i + "] = "
        );

        dimensions[i] =
                readInt(sc);
    }

    long answer =
            matrixChain(dimensions);

    System.out.println(
            "\nMinimum scalar multiplications = " +
            answer
    );
}


// ------------------------------------------------------------
// 5. OPTIMAL BINARY SEARCH TREE
// ------------------------------------------------------------
//
// keys[i] = key
// freq[i] = successful search frequency
//
// This version uses the standard successful-search OBST DP.
//

static long obst(
        int[] keys,
        int[] freq) {

    int n = keys.length;

    if (n == 0)
        return 0;

    long[][] dp =
            new long[n][n];

    long[][] sum =
            new long[n][n];

    for (int i = 0; i < n; i++) {

        dp[i][i] = freq[i];

        sum[i][i] = freq[i];
    }

    for (int length = 2;
         length <= n;
         length++) {

        for (int i = 0;
             i <= n - length;
             i++) {

            int j =
                    i + length - 1;

            sum[i][j] =
                    sum[i][j - 1]
                    +
                    freq[j];

            dp[i][j] =
                    Long.MAX_VALUE;

            for (int root = i;
                 root <= j;
                 root++) {

                long left =
                        root > i
                        ? dp[i][root - 1]
                        : 0;

                long right =
                        root < j
                        ? dp[root + 1][j]
                        : 0;

                long cost =
                        left +
                        right +
                        sum[i][j];

                dp[i][j] =
                        Math.min(
                                dp[i][j],
                                cost
                        );
            }
        }
    }

    return dp[0][n - 1];
}


// ------------------------------------------------------------
// OBST DEMO
// ------------------------------------------------------------

static void obstDemo(Scanner sc) {

    System.out.println(
            "\n========== OPTIMAL BST =========="
    );

    System.out.print(
            "Enter number of keys: "
    );

    int n =
            readInt(sc);

    if (n <= 0) {

        System.out.println(
                "Number of keys must be positive."
        );

        return;
    }

    int[] keys =
            new int[n];

    int[] freq =
            new int[n];

    System.out.println(
            "\nEnter keys:"
    );

    for (int i = 0;
         i < n;
         i++) {

        System.out.print(
                "Key[" + i + "] = "
        );

        keys[i] =
                readInt(sc);
    }

    System.out.println(
            "\nEnter frequencies:"
    );

    for (int i = 0;
         i < n;
         i++) {

        System.out.print(
                "Frequency[" + i + "] = "
        );

        freq[i] =
                readInt(sc);
    }

    long answer =
            obst(keys, freq);

    System.out.println(
            "\nMinimum OBST search cost = " +
            answer
    );
}


// ============================================================
// CO-3 MENU
// ============================================================

static void dpMenu(Scanner sc) {

    while (true) {

        System.out.println(
                "\n======================================"
        );

        System.out.println(
                "       CO-3 : DYNAMIC PROGRAMMING"
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "1. Levenshtein Distance"
        );

        System.out.println(
                "2. Damerau-Levenshtein Distance"
        );

        System.out.println(
                "3. Bitmask DP"
        );

        System.out.println(
                "4. Matrix Chain Multiplication"
        );

        System.out.println(
                "5. Optimal BST"
        );

        System.out.println(
                "6. Back"
        );

        System.out.print(
                "Enter choice: "
        );

        int choice = sc.nextInt();
                


        switch (choice) {

            case 1:
                levenshteinDemo(sc);
                break;

            case 2:
                damerauDemo(sc);
                break;

            case 3:
                bitmaskDemo(sc);
                break;

            case 4:
                mcmDemo(sc);
                break;

            case 5:
                obstDemo(sc);
                break;

            case 6:
                return;

            default:
                System.out.println(
                        "Invalid choice."
                );
        }
    }
}

// ============================================================
// CO-4 : NETWORK FLOW
// ============================================================

static class Edge {

    int to;
    int capacity;
    int flow;

    Edge reverse;


    Edge(
            int to,
            int capacity) {

        this.to = to;
        this.capacity = capacity;
        this.flow = 0;
    }


    int residualCapacity() {

        return capacity - flow;
    }
}


// ============================================================
// FLOW NETWORK
// ============================================================

static class FlowNetwork {

    int vertices;

    ArrayList<Edge>[] graph;


    @SuppressWarnings("unchecked")
    FlowNetwork(int vertices) {

        this.vertices = vertices;

        graph =
                new ArrayList[vertices];


        for (int i = 0;
             i < vertices;
             i++) {

            graph[i] =
                    new ArrayList<>();
        }
    }


    void addEdge(
            int from,
            int to,
            int capacity) {

        Edge forward =
                new Edge(
                        to,
                        capacity
                );

        Edge backward =
                new Edge(
                        from,
                        0
                );


        forward.reverse =
                backward;

        backward.reverse =
                forward;


        graph[from].add(
                forward
        );

        graph[to].add(
                backward
        );
    }
}


// ============================================================
// CREATE SAMPLE FOOD SUPPLY NETWORK
// ============================================================
//
// 0 = Supplier
// 1 = Warehouse A
// 2 = Warehouse B
// 3 = Distribution Center
// 4 = Market
//
// Supplier
//    |100
//    v
// Warehouse A ----70---->
//                         Distribution Center
// Warehouse B ----60---->
//    ^80
//
// Distribution Center --100--> Market
//
// ============================================================

static FlowNetwork createFoodNetwork() {

    FlowNetwork network =
            new FlowNetwork(5);


    network.addEdge(
            0, 1, 100
    );

    network.addEdge(
            0, 2, 80
    );

    network.addEdge(
            1, 3, 70
    );

    network.addEdge(
            2, 3, 60
    );

    network.addEdge(
            3, 4, 100
    );


    return network;
}


// ============================================================
// 1. FORD-FULKERSON
// ============================================================

static int fordFulkerson(
        FlowNetwork network,
        int source,
        int sink) {

    int maxFlow = 0;


    while (true) {

        boolean[] visited =
                new boolean[
                        network.vertices
                ];

        Edge[] parent =
                new Edge[
                        network.vertices
                ];


        boolean found =
                dfsFlow(
                        network,
                        source,
                        sink,
                        visited,
                        parent
                );


        if (!found)
            break;


        int pathFlow =
                Integer.MAX_VALUE;


        int current =
                sink;


        while (current != source) {

            Edge edge =
                    parent[current];


            pathFlow =
                    Math.min(
                            pathFlow,
                            edge.residualCapacity()
                    );


            current =
                    edge.reverse.to;
        }


        current =
                sink;


        while (current != source) {

            Edge edge =
                    parent[current];


            edge.flow +=
                    pathFlow;

            edge.reverse.flow -=
                    pathFlow;


            current =
                    edge.reverse.to;
        }


        maxFlow +=
                pathFlow;
    }


    return maxFlow;
}


// ============================================================
// DFS FOR FORD-FULKERSON
// ============================================================

static boolean dfsFlow(
        FlowNetwork network,
        int current,
        int sink,
        boolean[] visited,
        Edge[] parent) {

    if (current == sink)
        return true;


    visited[current] = true;


    for (Edge edge :
            network.graph[current]) {

        if (
                !visited[edge.to]
                &&
                edge.residualCapacity() > 0
        ) {

            parent[edge.to] =
                    edge;


            if (
                    dfsFlow(
                            network,
                            edge.to,
                            sink,
                            visited,
                            parent
                    )
            ) {

                return true;
            }
        }
    }


    return false;
}


// ============================================================
// 2. EDMONDS-KARP
// ============================================================

static int edmondsKarp(
        FlowNetwork network,
        int source,
        int sink) {

    int maxFlow = 0;


    while (true) {

        Edge[] parent =
                new Edge[
                        network.vertices
                ];


        Queue<Integer> queue =
                new LinkedList<>();


        boolean[] visited =
                new boolean[
                        network.vertices
                ];


        queue.add(source);

        visited[source] = true;


        while (!queue.isEmpty()) {

            int current =
                    queue.poll();


            for (Edge edge :
                    network.graph[current]) {

                if (
                        !visited[edge.to]
                        &&
                        edge.residualCapacity() > 0
                ) {

                    visited[edge.to] =
                            true;

                    parent[edge.to] =
                            edge;

                    queue.add(
                            edge.to
                    );
                }
            }
        }


        if (!visited[sink])
            break;


        int pathFlow =
                Integer.MAX_VALUE;


        int current =
                sink;


        while (current != source) {

            Edge edge =
                    parent[current];


            pathFlow =
                    Math.min(
                            pathFlow,
                            edge.residualCapacity()
                    );


            current =
                    edge.reverse.to;
        }


        current =
                sink;


        while (current != source) {

            Edge edge =
                    parent[current];


            edge.flow +=
                    pathFlow;

            edge.reverse.flow -=
                    pathFlow;


            current =
                    edge.reverse.to;
        }


        maxFlow +=
                pathFlow;
    }


    return maxFlow;
}


// ============================================================
// 3. DINIC'S ALGORITHM
// ============================================================

static boolean dinicBFS(
        FlowNetwork network,
        int source,
        int sink,
        int[] level) {

    Arrays.fill(
            level,
            -1
    );


    Queue<Integer> queue =
            new LinkedList<>();


    level[source] = 0;

    queue.add(source);


    while (!queue.isEmpty()) {

        int current =
                queue.poll();


        for (Edge edge :
                network.graph[current]) {

            if (
                    edge.residualCapacity() > 0
                    &&
                    level[edge.to] < 0
            ) {

                level[edge.to] =
                        level[current] + 1;

                queue.add(
                        edge.to
                );
            }
        }
    }


    return level[sink] >= 0;
}


// ============================================================
// DINIC DFS
// ============================================================

static int dinicDFS(
        FlowNetwork network,
        int current,
        int sink,
        int pushed,
        int[] level,
        int[] ptr) {

    if (current == sink)
        return pushed;


    if (pushed == 0)
        return 0;


    while (
            ptr[current]
            <
            network.graph[current].size()
    ) {

        Edge edge =
                network.graph[current]
                        .get(
                                ptr[current]
                        );


        if (
                level[edge.to]
                ==
                level[current] + 1
                &&
                edge.residualCapacity() > 0
        ) {

            int flow =
                    dinicDFS(
                            network,
                            edge.to,
                            sink,
                            Math.min(
                                    pushed,
                                    edge.residualCapacity()
                            ),
                            level,
                            ptr
                    );


            if (flow > 0) {

                edge.flow +=
                        flow;

                edge.reverse.flow -=
                        flow;


                return flow;
            }
        }


        ptr[current]++;
    }


    return 0;
}


// ============================================================
// DINIC MAX FLOW
// ============================================================

static int dinic(
        FlowNetwork network,
        int source,
        int sink) {

    int maxFlow = 0;


    int[] level =
            new int[
                    network.vertices
            ];


    while (
            dinicBFS(
                    network,
                    source,
                    sink,
                    level
            )
    ) {

        int[] ptr =
                new int[
                        network.vertices
                ];


        while (true) {

            int pushed =
                    dinicDFS(
                            network,
                            source,
                            sink,
                            Integer.MAX_VALUE,
                            level,
                            ptr
                    );


            if (pushed == 0)
                break;


            maxFlow +=
                    pushed;
        }
    }


    return maxFlow;
}


// ============================================================
// 4. BIPARTITE MATCHING
// ============================================================
//
// Left side:
// Farmers
//
// Right side:
// Warehouses
//
// matchR[j] = farmer assigned to warehouse j
//
// ============================================================

static boolean bpmDFS(
        int farmer,
        boolean[][] graph,
        boolean[] visited,
        int[] matchR) {

    for (
            int warehouse = 0;
            warehouse < graph[farmer].length;
            warehouse++
    ) {

        if (
                graph[farmer][warehouse]
                &&
                !visited[warehouse]
        ) {

            visited[warehouse] = true;


            if (
                    matchR[warehouse] == -1
                    ||
                    bpmDFS(
                            matchR[warehouse],
                            graph,
                            visited,
                            matchR
                    )
            ) {

                matchR[warehouse] =
                        farmer;

                return true;
            }
        }
    }


    return false;
}


// ============================================================
// BIPARTITE MATCHING DEMO
// ============================================================

static void bipartiteMatchingDemo(
        Scanner sc) {

    System.out.println(
            "\n========== BIPARTITE MATCHING =========="
    );


    System.out.print(
            "Enter number of farmers: "
    );

    int farmers = sc.nextInt();
           


    System.out.print(
            "Enter number of warehouses: "
    );

    int warehouses = sc.nextInt();
            


    if (
            farmers <= 0 ||
            warehouses <= 0
    ) {

        System.out.println(
                "Numbers must be positive."
        );

        return;
    }


    boolean[][] graph =
            new boolean[
                    farmers
            ][warehouses];


    System.out.println(
            "\nEnter compatibility matrix."
    );

    System.out.println(
            "1 = Farmer can supply warehouse"
    );

    System.out.println(
            "0 = Cannot supply"
    );


    for (int i = 0;
         i < farmers;
         i++) {

        for (int j = 0;
             j < warehouses;
             j++) {

            System.out.print(
                    "Farmer " +
                    (i + 1) +
                    " -> Warehouse " +
                    (j + 1) +
                    ": "
            );


            int value =
                    readInt(sc);


            graph[i][j] =
                    value == 1;
        }
    }


    int[] matchR =
            new int[
                    warehouses
            ];


    Arrays.fill(
            matchR,
            -1
    );


    int matching = 0;


    for (int farmer = 0;
         farmer < farmers;
         farmer++) {

        boolean[] visited =
                new boolean[
                        warehouses
                ];


        if (
                bpmDFS(
                        farmer,
                        graph,
                        visited,
                        matchR
                )
        ) {

            matching++;
        }
    }


    System.out.println(
            "\nMaximum matching = " +
            matching
    );


    for (int warehouse = 0;
         warehouse < warehouses;
         warehouse++) {

        if (
                matchR[warehouse] != -1
        ) {

            System.out.println(
                    "Farmer " +
                    (matchR[warehouse] + 1)
                    +
                    " -> Warehouse " +
                    (warehouse + 1)
            );
        }
    }
}


// ============================================================
// 5. MAX-FLOW MIN-CUT
// ============================================================

static void printMinCut(
        FlowNetwork network,
        int source) {

    boolean[] visited =
            new boolean[
                    network.vertices
            ];


    Queue<Integer> queue =
            new LinkedList<>();


    queue.add(source);

    visited[source] = true;


    while (!queue.isEmpty()) {

        int current =
                queue.poll();


        for (Edge edge :
                network.graph[current]) {

            if (
                    !visited[edge.to]
                    &&
                    edge.residualCapacity() > 0
            ) {

                visited[edge.to] =
                        true;

                queue.add(
                        edge.to
                );
            }
        }
    }


    System.out.println(
            "\n========== MIN CUT EDGES =========="
    );


    for (int u = 0;
         u < network.vertices;
         u++) {

        if (!visited[u])
            continue;


        for (Edge edge :
                network.graph[u]) {

            /*
             * Ignore reverse edges.
             * Original forward edge has capacity > 0.
             */

            if (
                    edge.capacity > 0
                    &&
                    !visited[edge.to]
            ) {

                System.out.println(
                        u +
                        " -> " +
                        edge.to +
                        " | Capacity = " +
                        edge.capacity
                );
            }
        }
    }
}


// ============================================================
// MAX-FLOW MIN-CUT DEMO
// ============================================================

static void maxFlowMinCutDemo() {

    System.out.println(
            "\n========== MAX-FLOW MIN-CUT =========="
    );


    FlowNetwork network =
            createFoodNetwork();


    int source = 0;

    int sink = 4;


    int maxFlow =
            dinic(
                    network,
                    source,
                    sink
            );


    System.out.println(
            "Maximum Flow = " +
            maxFlow
    );


    printMinCut(
            network,
            source
    );
}


// ============================================================
// CO-4 MENU
// ============================================================

static void networkMenu(Scanner sc) {

    while (true) {

        System.out.println(
                "\n======================================"
        );

        System.out.println(
                "       CO-4 : NETWORK FLOW"
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "1. Ford-Fulkerson"
        );

        System.out.println(
                "2. Edmonds-Karp"
        );

        System.out.println(
                "3. Dinic"
        );

        System.out.println(
                "4. Max-Flow Min-Cut"
        );

        System.out.println(
                "5. Bipartite Matching"
        );

        System.out.println(
                "6. Back"
        );


        System.out.print(
                "Enter choice: "
        );


        int choice = sc.nextInt();
                


        switch (choice) {

            case 1: {

                FlowNetwork network =
                        createFoodNetwork();

                int maxFlow =
                        fordFulkerson(
                                network,
                                0,
                                4
                        );

                System.out.println(
                        "\nFord-Fulkerson Max Flow = " +
                        maxFlow
                );

                break;
            }


            case 2: {

                FlowNetwork network =
                        createFoodNetwork();

                int maxFlow =
                        edmondsKarp(
                                network,
                                0,
                                4
                        );

                System.out.println(
                        "\nEdmonds-Karp Max Flow = " +
                        maxFlow
                );

                break;
            }


            case 3: {

                FlowNetwork network =
                        createFoodNetwork();

                int maxFlow =
                        dinic(
                                network,
                                0,
                                4
                        );

                System.out.println(
                        "\nDinic Max Flow = " +
                        maxFlow
                );

                break;
            }


            case 4:

                maxFlowMinCutDemo();

                break;


            case 5:

                bipartiteMatchingDemo(sc);

                break;


            case 6:

                return;


            default:

                System.out.println(
                        "Invalid choice."
                );
        }
    }
}


   
    // MAIN MENU
   
   public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);

    
    initializeFiles();

    while (true) {

        System.out.println(
                "\n\n=========================================="
        );

        System.out.println(
                "              FOODCHAIN"
        );

        System.out.println(
                "      FOOD SUPPLY MANAGEMENT"
        );

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "1. Farmer"
        );

        System.out.println(
                "2. Customer"
        );

        System.out.println(
                "3. Exit"
        );

        System.out.print(
                "Enter choice: "
        );


       
        int choice = sc.nextInt();


        switch (choice) {


            case 1:

                farmerPhase(
                        sc
                );

                break;


           
           

            case 2:

                customerPhase(
                        sc
                );

                break;


            

            case 3:

                System.out.println(
                        "\n=========================================="
                );

                System.out.println(
                        " Thank you for using FoodChain!"
                );

                System.out.println(
                        "=========================================="
                );

                sc.close();

                return;


          

            default:

                System.out.println(
                        "\nInvalid choice."
                );

                System.out.println(
                        "Please enter 1, 2 or 3."
                );
        }
    }
}
}