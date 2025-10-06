#!/bin/bash

# Script to update all package declarations from cheatclient to nimbusware

echo "Updating package declarations..."

# Find all Java files and update package declarations
find src/main/java/com/example/nimbusware -name "*.java" -type f | while read file; do
    echo "Updating: $file"
    
    # Update package declaration
    sed -i 's/package com\.example\.cheatclient/package com.example.nimbusware/g' "$file"
    
    # Update import statements
    sed -i 's/import com\.example\.cheatclient/import com.example.nimbusware/g' "$file"
    
    # Update class references
    sed -i 's/CheatClient\.INSTANCE/NimbusWare.INSTANCE/g' "$file"
    sed -i 's/CheatClient client/NimbusWare client/g' "$file"
    sed -i 's/CheatClient this/NimbusWare this/g' "$file"
    sed -i 's/CheatClient(/NimbusWare(/g' "$file"
    sed -i 's/CheatClient\./NimbusWare./g' "$file"
    sed -i 's/CheatClient /NimbusWare /g' "$file"
    sed -i 's/CheatClient,/NimbusWare,/g' "$file"
    sed -i 's/CheatClient;/NimbusWare;/g' "$file"
    sed -i 's/CheatClient)/NimbusWare)/g' "$file"
    sed -i 's/CheatClient>/NimbusWare>/g' "$file"
    sed -i 's/CheatClient\[/NimbusWare[/g' "$file"
    sed -i 's/CheatClient\*/NimbusWare*/g' "$file"
    sed -i 's/CheatClient?/NimbusWare?/g' "$file"
    sed -i 's/CheatClient&/NimbusWare&/g' "$file"
    sed -i 's/CheatClient|/NimbusWare|/g' "$file"
    sed -i 's/CheatClient+/NimbusWare+/g' "$file"
    sed -i 's/CheatClient-/NimbusWare-/g' "$file"
    sed -i 's/CheatClient=/NimbusWare=/g' "$file"
    sed -i 's/CheatClient!/NimbusWare!/g' "$file"
    sed -i 's/CheatClient@/NimbusWare@/g' "$file"
    sed -i 's/CheatClient#/NimbusWare#/g' "$file"
    sed -i 's/CheatClient%/NimbusWare%/g' "$file"
    sed -i 's/CheatClient\^/NimbusWare^/g' "$file"
    sed -i 's/CheatClient~/NimbusWare~/g' "$file"
    sed -i 's/CheatClient`/NimbusWare`/g' "$file"
    sed -i 's/CheatClient{/NimbusWare{/g' "$file"
    sed -i 's/CheatClient}/NimbusWare}/g' "$file"
    sed -i 's/CheatClient</NimbusWare</g' "$file"
    sed -i 's/CheatClient>/NimbusWare>/g' "$file"
    sed -i 's/CheatClient,/NimbusWare,/g' "$file"
    sed -i 's/CheatClient\./NimbusWare./g' "$file"
    sed -i 's/CheatClient\//NimbusWare\//g' "$file"
    sed -i 's/CheatClient\\/NimbusWare\\/g' "$file"
    sed -i 's/CheatClient:/NimbusWare:/g' "$file"
    sed -i 's/CheatClient;/NimbusWare;/g' "$file"
    sed -i 's/CheatClient"/NimbusWare"/g' "$file"
    sed -i 's/CheatClient'"'"'/NimbusWare'"'"'/g' "$file"
    sed -i 's/CheatClient\s/NimbusWare /g' "$file"
done

echo "Package update complete!"