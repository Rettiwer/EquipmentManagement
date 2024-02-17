<script lang="ts">
    import NavItem from "$lib/components/NavItem.svelte";
    import Logo from "$lib/components/Logo.svelte";
    import {page} from "$app/stores";
    import {IconLogout, IconTool, IconUser, IconUsers} from "@tabler/icons-svelte";

    let visibleMobileMenu = false;

    console.log($page.url)
</script>

<nav>
    <div class="navbar bg-primary relative text-white md:hidden">
        <div class="flex flex-1 pl-4">
            <span class="font-mono font-black text-2xl">EM</span>
            <div class="divider divider-horizontal before:bg-base-100 after:bg-base-100"></div>
            <span class="text-sm font-mono font-medium">Equipment Management</span>
        </div>
        <div>
            <div class="btn btn-ghost btn-circle" on:click={() => (visibleMobileMenu = !visibleMobileMenu)}>
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7" /></svg>
            </div>
        </div>
    </div>

    <aside class="w-full z-50 absolute md:block md:relative md:w-64 md:h-screen" class:hidden={!visibleMobileMenu}>
        <section class="flex flex-col justify-between h-full px-3 py-5 bg-primary">

            <Logo/>

            <ul class="space-y-2">
                <li>
                    <a href="/invoices" on:click={() => (visibleMobileMenu = !visibleMobileMenu)}>
                        <NavItem title="Equipment" selected={$page.url.pathname.startsWith("/invoice")} >
                            <span slot="before">
                                <IconTool />
                            </span>
                        </NavItem>
                    </a>
                </li>
                <li>
                    <a href="/users" on:click={() => (visibleMobileMenu = !visibleMobileMenu)}>
                        <NavItem title="Users" selected={$page.url.pathname.startsWith("/users")}>
                            <span slot="before">
                                <IconUsers />
                            </span>
                        </NavItem>
                    </a>
                </li>
            </ul>
            <ul class="space-y-2 mt-10">
                <li>
                    <a href="/">
                        <NavItem title={$page.data.user.firstname + ', ' + $page.data.user?.lastname} selected>
                            <span slot="before">
                                <IconUser />
                            </span>
                        </NavItem>
                    </a>
                </li>
                <li>
                    <a href="/logout" >
                        <NavItem title="Logout">
                            <span slot="before">
                                <IconLogout />
                            </span>
                        </NavItem>
                    </a>
                </li>
            </ul>
        </section>
    </aside>
</nav>

