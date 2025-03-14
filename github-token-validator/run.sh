# Initialize Gradle wrapper (if not already present)
gradle wrapper

# Clean and build the JAR
./gradlew clean build --refresh-dependencies

# Create a fat JAR with dependencies
# ./gradlew build --refresh-dependencies
