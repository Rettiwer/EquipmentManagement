<script lang="ts">
    import Card from "$lib/components/Card.svelte";
    import Input from "$lib/components/Input.svelte";
    import Checkbox from "$lib/components/Checkbox.svelte";
    import Button from "$lib/components/Button.svelte";
    import {redirect} from "@sveltejs/kit";
    import {type AuthenticationRequest, login} from "$lib/api/auth";

    let request: AuthenticationRequest = {
        email: "",
        password: "",
    }

    function onSubmit() {
        console.log("asd");
        login(request);
            //
            // let accessTokenTime = new Date();
            // accessTokenTime.setDate(accessTokenTime.getDate() + 1);
            // event.cookies.set('accessToken', result.access_token, {
            //     path: '/',
            //     sameSite: 'lax',
            //     httpOnly: true,
            //     expires: accessTokenTime,
            //     secure: false
            // });
            //
            // let refreshTokenTime = new Date();
            // refreshTokenTime.setDate(refreshTokenTime.getDate() + 7);
            // event.cookies.set('refreshToken', result.refresh_token, {
            //     path: '/',
            //     sameSite: 'lax',
            //     httpOnly: true,
            //     expires: refreshTokenTime,
            //     secure: false
            // });
    }

</script>

<svelte:head>
    <title>Login - EM</title>
</svelte:head>

<Card
        title="Log In">
    <span slot="content">

        <!--{#if errors && form?.message != null }-->
        <!--    <p class="text-red-500">{form.message}</p>-->
        <!--{/if}-->

        <form on:submit|preventDefault={onSubmit}>
            <Input
                    type="email"
                    label="Email"
                    name="email"
                    placeholder="email"
                    required
                    autofocus
                    autocomplete="username"
                    bind:value={request.email}
            />

            <Input
                    type="password"
                    label="Password"
                    name="password"
                    placeholder="password"
                    required
                    autocomplete="current-password"
                    bind:value={request.password}
            />

            <Checkbox name="remember" label="Remember me"/>

            <div class="flex items-center justify-end mt-4">

                    <a href="/resetPassword" class="link link-hover">
                        Reset password
                    </a>

                    <Button class="ml-4" type="submit" disabled={false}>
                        LOG IN
                    </Button>
            </div>
        </form>

    </span>

</Card>