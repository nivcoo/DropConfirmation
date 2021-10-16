<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![Quality][quality-shield]][quality-url]

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/TamrielNetwork/DropConfirm">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">DropConfirm</h3>

  <p align="center">
    Prevents Dropping the wrong items.
    <br />
    <a href="https://github.com/TamrielNetwork/DropConfirm"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/TamrielNetwork/DropConfirm">View Demo</a>
    ·
    <a href="https://github.com/TamrielNetwork/DropConfirm/issues">Report Bug</a>
    ·
    <a href="https://github.com/TamrielNetwork/DropConfirm/issues">Request Feature</a>
  </p>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## About The Project

### Description

DropConfirm is a plugin that prevents you from dropping the wrong items.

This plugin is perfect for any server wanting their players to be extra safe when handling items!

### Features

* Prevent item dropping
* Whitelist
* Renamed Items
* World Blacklist

### Built With

* [Gradle 7](https://docs.gradle.org/7.2/release-notes.html)
* [OpenJDK 16](https://openjdk.java.net/projects/jdk/16/)

<!-- GETTING STARTED -->

## Getting Started

To get the plugin running on your server follow these simple steps.

### Commands and Permissions

1. Permission: `dropconfirm.reload`

* Command: `/drop reload`
* Description: Reload the plugin

2. Permission: `DEFAULT`

* Command: `/drop`
* Description: Main command

3. Permission: `dropconfirm.bypass`

* Description: Bypass the drop check

### Configuration

```
confirm_only_renamed: false

whitelisted_items: [ ]

seconds_before_reset: 5

per_item_confirmation: false

reset_confirm_after_drop: true

blacklisted_worlds: [ ]

messages:
  prefix: "&7[&c&lConfirm&7] "
  #use {0} to use seconds before cancel
  cancel_message: "Confirm within  {0} seconds!"
  no_permission: "&7You don't have enough permissions!"
```

<!-- ROADMAP -->

## Roadmap

See the [open issues](https://github.com/TamrielNetwork/DropConfirm/issues) for a list of proposed features (and known
issues).

<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to be, learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<!-- LICENSE -->

## License

Distributed under the GNU General Public License v3.0. See `LICENSE` for more information.

<!-- CONTACT -->

## Contact

Leopold Meinel - [@TamrielN](https://twitter.com/TamrielN) - Twitter

Leopold Meinel - [contact@tamriel.me](mailto:contact@tamriel.me) - email

Project Link: [https://github.com/TamrielNetwork/DropConfirm](https://github.com/TamrielNetwork/DropConfirm)

<!-- ACKNOWLEDGEMENTS -->

## Acknowledgements

* [README.md - othneildrew](https://github.com/othneildrew/Best-README-Template)
* [Initial Development - nivcoo](https://github.com/nivcoo/DropConfirmation)

<!-- MARKDOWN LINKS & IMAGES -->

[contributors-shield]: https://img.shields.io/github/contributors-anon/TamrielNetwork/DropConfirm?style=for-the-badge

[contributors-url]: https://github.com/TamrielNetwork/DropConfirm/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks/TamrielNetwork/DropConfirm?label=Forks&style=for-the-badge

[forks-url]: https://github.com/TamrielNetwork/DropConfirm/network/members

[stars-shield]: https://img.shields.io/github/stars/TamrielNetwork/DropConfirm?style=for-the-badge

[stars-url]: https://github.com/TamrielNetwork/DropConfirm/stargazers

[issues-shield]: https://img.shields.io/github/issues/TamrielNetwork/DropConfirm?style=for-the-badge

[issues-url]: https://github.com/TamrielNetwork/DropConfirm/issues

[license-shield]: https://img.shields.io/github/license/TamrielNetwork/DropConfirm?style=for-the-badge

[license-url]: https://github.com/TamrielNetwork/DropConfirm/blob/main/LICENSE

[quality-shield]: https://img.shields.io/scrutinizer/quality/g/TamrielNetwork/DropConfirm?label=quality&style=for-the-badge

[quality-url]: https://scrutinizer-ci.com/g/TamrielNetwork/DropConfirm/reports/
