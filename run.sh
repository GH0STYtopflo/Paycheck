#!/usr/bin/env bash

set -euo pipefail

ask_install() {
    local name="$1"

    read -rp "$name is missing. Install it now? [y/N]: " answer

    if [[ ! "$answer" =~ ^[Yy]$ ]]; then
        echo "$name is required."
        exit 1
    fi
}

detect_package_manager() {
    if command -v apt-get >/dev/null 2>&1; then
        PKG_MANAGER="apt"
    elif command -v dnf >/dev/null 2>&1; then
        PKG_MANAGER="dnf"
    elif command -v pacman >/dev/null 2>&1; then
        PKG_MANAGER="pacman"
    elif command -v zypper >/dev/null 2>&1; then
        PKG_MANAGER="zypper"
    else
        echo "Unsupported package manager."
        exit 1
    fi
}

install_git() {
    case "$PKG_MANAGER" in
        apt)
            sudo apt-get update
            sudo apt-get install -y git
            ;;
        dnf)
            sudo dnf install -y git
            ;;
        pacman)
            sudo pacman -S --needed --noconfirm git
            ;;
        zypper)
            sudo zypper install -y git
            ;;
    esac
}

install_maven() {
    case "$PKG_MANAGER" in
        apt)
            sudo apt-get update
            sudo apt-get install -y maven
            ;;
        dnf)
            sudo dnf install -y maven
            ;;
        pacman)
            sudo pacman -S --needed --noconfirm maven
            ;;
        zypper)
            sudo zypper install -y maven
            ;;
    esac
}

install_jdk21() {
    case "$PKG_MANAGER" in
        apt)
            sudo apt-get update
            sudo apt-get install -y openjdk-21-jdk
            ;;
        dnf)
            sudo dnf install -y java-21-openjdk-devel
            ;;
        pacman)
            sudo pacman -S --needed --noconfirm jdk21-openjdk
            ;;
        zypper)
            sudo zypper install -y java-21-openjdk-devel
            ;;
    esac
}

get_java_major_version() {
    if ! command -v java >/dev/null 2>&1; then
        echo 0
        return
    fi

    java -version 2>&1 \
        | awk -F '"' '/version/ {print $2}' \
        | awk -F. '{print ($1 == 1 ? $2 : $1)}'
}

detect_package_manager

echo "Checking prerequisites..."

# Git
if ! command -v git >/dev/null 2>&1; then
    ask_install "Git"
    install_git
fi

echo "✓ Git found"

# Java
JAVA_VERSION=$(get_java_major_version)

if (( JAVA_VERSION < 21 )); then
    if (( JAVA_VERSION == 0 )); then
        ask_install "JDK 21"
    else
        echo "Found JDK $JAVA_VERSION, but JDK 21+ is required."
        ask_install "JDK 21"
    fi

    install_jdk21
    JAVA_VERSION=$(get_java_major_version)

    if (( JAVA_VERSION < 21 )); then
        echo "Failed to install or detect JDK 21."
        exit 1
    fi
fi

echo "✓ JDK $JAVA_VERSION found"

# Maven
if ! command -v mvn >/dev/null 2>&1; then
    ask_install "Maven"
    install_maven
fi

echo "✓ Maven found"

echo "Starting application..."
mvn clean javafx:run
