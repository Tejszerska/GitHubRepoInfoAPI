# GitHubRepoInfoAPI

GitHubRepoInfoAPI is a tool for fetching a list of a GitHub user's repositories that are not forks, and obtaining information about their branches and latest commits.

## Features

- Fetching a list of a GitHub user's repositories that are not forks.
- Displaying detailed information about the branches of each repository.
- Displaying the latest commits in each branch of the repository.

## Requirements

- Java 21
- Maven

## Installation

1. Clone the repository:
    git clone https://github.com/Tejszerska/GitHubRepoInfoAPI.git

2. Navigate to the project directory:
    cd GitHubRepoInfoAPI

3. Build the project using Maven:
    mvn clean install

## Usage

1. Run the application:
    java -jar target/GitHubRepoInfoAPI-1.0-SNAPSHOT.jar

## Endpoint

- **URL**: `/ghinfo/{username}`
- **Method**: `GET`
- **Parameters**:
  - `Authorization` (string) - GitHub token in the format `ghp_xxx...`

**Example**:
GET ghinfo/tejszerska?YOUR_GITHUB_TOKEN

## Author

- [Tejszerska](https://github.com/Tejszerska)

## License

This project is licensed under the terms of the Unlicense.
